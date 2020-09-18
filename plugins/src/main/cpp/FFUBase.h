//
// Created by strong on 2019/1/10.
//

#ifndef CODE_FFUBASE_H
#define CODE_FFUBASE_H
enum FFU_ERR{
    YS_SUCCESS = 0,
    ERR_A0_SEND = 1,
    ERR_A0_RECV = 2,
    ERR_A0_DIRC = 3,
    ERR_A0_STAT = 4,

    ERR_A1_SEND = 101,
    ERR_A1_RECV = 102,

    ERR_A2_SEND = 201,
    ERR_A2_RECV = 202,
    ERR_A2_DIRC = 203,
    ERR_A2_STAT = 204,



};
typedef  struct
{
    int dCMDSign;              // 包头识别字段
    char  bCMD;                // FILE_FFU命令，分为A0、A1、A2，见下面的宏定义
    char  bReserve0[1];
    char  bDirection;              // 命令方向，host写为FFU_Direction_Write，device回应为FFU_Direction_Read
    char  bStatus;              // 命令状态，host写时清0，device回应为FFU_CMD_STATUS_OK或FFU_CMD_STATUS_FAILED
    char  m_FWVersion[16];          // A2命令使用字段，device将固件版本号回应到此字段
    int dFWChecksum;            // A1命令使用字段，表明FW的CheckSum
    short wTotalFWSector ;
    char bA1Status ;

    char  bCMDHeadCheckSum;      // 以上31byte的checksum
    char  bCmdRequestParam[480];
}StPriCmd;

const static int LEN_16K = 16*1024;
const static int LEN_4K = 4*1024;

class FFUBase {
public:
    FFUBase(const char* path);
    ~FFUBase();

public:
    // 返回值：负数对应的正数表示linux的错误码，正数表示文件标识符
    int InitFile();
    /* 发送A2，切换成功返回0，失败返回错误码
     * pDevFwVer：保存版本号，长度不要小于16byte
    */
    int GetDeviceFwVer(char* pDevFwVer);

    //  发送A3，切换成功返回0，失败返回错误码
    int RecoveryFactorySet();

public:
    virtual void getA1HeadCheckSum(StPriCmd* stCmd){}
    // 获取命令头校验码
    char getCheckSum(char *buff, int size);
    virtual void getCmdHead(char cmdCode, StPriCmd* stCmd);
    int getCodeCheckSum(char *pFwBuff, int size);
    // 写一段buff到文件
    int writeBuffToFile(char * buff, int size);
    // 读一段文件到buff
    int readFileToBuff(char *buff, int size);
    void resetFile();
    void moveFileToStart();


    int sendCmd(char cmd ,char *result, size_t result_size) ;

protected:
    int mHeadLen;
private:
    char *pFilePath;
    int mFileID;
};
#endif //CODE_FFUBASE_H
