//
// Created by strong on 2019/1/10.
//

#include <cstring>
#include "FFUBase.h"
#include <fcntl.h>
#include <sys/stat.h>
#include <errno.h>
#include <malloc.h>
#include <unistd.h>
#include <stdlib.h>
#include "Entry.h"

const char* pFileName = "ysPri.bin";
FFUBase::FFUBase(const char* path)
{
    mHeadLen = 32;
    int buffLen = strlen(path)+strlen(pFileName)+1;
    pFilePath = new char[buffLen];
    memset((void*)pFilePath, 0, buffLen);

    memcpy(pFilePath, path, strlen(path));
    memcpy(pFilePath+strlen(path), pFileName, strlen(pFileName));
}
FFUBase::~FFUBase(){
    if (NULL != pFilePath)
    {
        delete []pFilePath;
        pFilePath = NULL;
    }
}

int FFUBase::InitFile()
{
    // 创建文件
    //https://www.mobibrw.com/2017/6874
    //https://stackoverflow.com/questions/584210/unix-o-creat-flag-without-mode-specified
    mFileID = open(pFilePath, O_RDWR|O_CREAT|O_DIRECT, S_IRWXU|S_IRWXG|S_IRWXO);
    if (mFileID>0)
    {
        ftruncate(mFileID,0);
        /* 重新设置文件偏移量 */
        lseek(mFileID,0,SEEK_SET);
    }
    LOGD("InitFile -- %d -- errno:%d",mFileID,errno);

    return mFileID;
}

void FFUBase::resetFile()
{
    if (mFileID>0)
    {
        ftruncate(mFileID,0);
        /* 重新设置文件偏移量 */
        lseek(mFileID,0,SEEK_SET);
    }
}

void FFUBase::moveFileToStart()
{
    if (mFileID>0)
    {
        /* 重新设置文件偏移量 */
        lseek(mFileID,0,SEEK_SET);
    }
}

// 获取命令头校验码
char FFUBase::getCheckSum(char *buff, int size)
{
    char checkSum = 0;
    char *p = buff;
    for (int i = 0; i < size; ++i) {
        checkSum ^= *p;
        if (i == size-1)
            break;
        p++;
    }

    return checkSum;
}

// 创建命令头
void FFUBase::getCmdHead(char cmdCode, StPriCmd* stCmd)
{
    stCmd->dCMDSign = 0x66554646;
    stCmd->bCMD = cmdCode;
    memset(stCmd->bReserve0,0, sizeof(stCmd->bReserve0));
    stCmd->bDirection = 1;
    stCmd->bStatus = 0 ;
    memset(stCmd->m_FWVersion,0, sizeof(stCmd->m_FWVersion));
    stCmd->dFWChecksum = 0;
    stCmd->wTotalFWSector = 0;
    stCmd->bA1Status = 0;
    stCmd->bCMDHeadCheckSum = getCheckSum((char*)stCmd,31);


}

int FFUBase::writeBuffToFile(char * buff, int size)
{
    resetFile();
    // 申请块对齐内存
    char *c = NULL;
    int pagesize = getpagesize();
    int ret = posix_memalign((void**)&c, pagesize, LEN_16K);
    if (ret)
        return errno;

    // 写
    int alreadyWriteSize = 0;
    int writeSize = 0;
    while (alreadyWriteSize<size)
    {
        memset(c, 0, LEN_16K);
        if (size - alreadyWriteSize < LEN_16K)
            writeSize = size - alreadyWriteSize;
        else
            writeSize = LEN_16K;

        memcpy(c, buff+alreadyWriteSize, writeSize);
        int len = write(mFileID, c, writeSize);
        if(len != writeSize)
        {
            ret = errno;
            break;
        }
        alreadyWriteSize += writeSize;
    }

    free(c);
    c = NULL;
    return ret;
}

// 读一段文件到buff
int FFUBase::readFileToBuff(char *buff, int size)
{
    int ret = 0;
    char *c = NULL;//(char*)malloc(BUFF_LEN);
    int readSize = (size+LEN_4K-1)/LEN_4K*LEN_4K;
    ret = posix_memalign((void**)&c, getpagesize(), readSize);
    if (ret)
    {
        ret = errno;
    } else{
        memset(c, 0, size);
        int len =  read(mFileID, c, size);
        if(len<=0)
        {
            ret = errno;
        } else{
            memcpy(buff, c, size);
        }
    }

    free(c);
    c = NULL;
    return ret;
}

int FFUBase::getCodeCheckSum(char *pFwBuff, int size)
{
    int checkSum = 0;
    int* p = (int*)pFwBuff;
    for (int i=0; i<size/4; i++)
    {
        checkSum ^= (*p);
        if (i == size/4 - 1)
            break;
        p++;
    }
    return checkSum;
}

// 0表示正常，非0表示错误码
int FFUBase::GetDeviceFwVer(char* pDevFwVer)
{
    // 初始化文件
   resetFile();

    char buff[LEN_4K] = {0};
    StPriCmd stCmd = {0};
    getCmdHead(0xA2, &stCmd);
    // 写A2
    memcpy(buff, &stCmd, sizeof(stCmd));
    int ret = writeBuffToFile(buff, LEN_4K);
    if (0 != ret)
        return ERR_A2_SEND;

    // 读A2，校验特殊字段
    lseek(mFileID,0,SEEK_SET);
    memset(buff, 0, LEN_4K);
    ret = readFileToBuff(buff, LEN_4K);
    if (0 != ret)
        return ERR_A2_RECV;

    memcpy(&stCmd,buff, sizeof(stCmd));
    if (0 != stCmd.bDirection)
        return ERR_A2_DIRC;
    if (1 != stCmd.bStatus)
        return ERR_A2_STAT;

    memcpy(pDevFwVer, stCmd.m_FWVersion, sizeof(stCmd.m_FWVersion));
    // 设置返回参数
    return YS_SUCCESS;
}



int FFUBase::sendCmd(char cmd ,char *result, size_t result_size) {
    // 初始化文件
    resetFile();

    char buff[LEN_4K] = {0};
    StPriCmd stCmd = {0};
    getCmdHead(cmd, &stCmd);
    // 写A2
    memcpy(buff, &stCmd, sizeof(stCmd));
    int ret = writeBuffToFile(buff, LEN_4K);
    if (0 != ret)
        return ERR_A2_SEND;

    // 读A2，校验特殊字段
    lseek(mFileID,0,SEEK_SET);
    memset(buff, 0, LEN_4K);
    ret = readFileToBuff(buff, LEN_4K);
    if (0 != ret)
        return ERR_A2_RECV;

    memcpy(&stCmd,buff, sizeof(stCmd));
    if (0 != stCmd.bDirection)
        return ERR_A2_DIRC;
    if (1 != stCmd.bStatus)
        return ERR_A2_STAT;

    memcpy(result, buff + 32, result_size);
    // 设置返回参数
    return YS_SUCCESS;

}

int FFUBase::RecoveryFactorySet()
{
    StPriCmd stCmd = {0};
    getCmdHead(0xA3, &stCmd);

    return writeBuffToFile((char*)&stCmd, sizeof(stCmd));
}

