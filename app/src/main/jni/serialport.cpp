#include <stdio.h>
#include <stdlib.h>
#include <assert.h>
#include <string.h>

#include <termios.h>
#include <unistd.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <fcntl.h>

#include <jni.h>
#include <android/log.h>

// #define LOGD(fmt, args...) __android_log_print(ANDROID_LOG_DEBUG, "#DEBUG#", fmt, ##args)
// #define LOGE(fmt, args...) __android_log_print(ANDROID_LOG_ERROR, "#ERROR#", fmt, ##args)

static const char gClassName[] = "com/lztek/toolkit/SerialPort";

JNIEXPORT jobject JNICALL com_open(JNIEnv *env, jclass clazz, jstring path, jint baudrate,
		jint dataBit, jint parity, jint stopBits, jint dataFlow);
JNIEXPORT jint JNICALL com_close(JNIEnv * env, jclass clazz, jobject fileDescriptor);

//定义方法隐射关系
static JNINativeMethod gExportMethods[] =
{
	{
		"_open",
		"(Ljava/lang/String;IIIII)Ljava/io/FileDescriptor;",
		(void*)com_open
	},
	{
		"_close",
		"(Ljava/io/FileDescriptor;)I",
		(void*)com_close
	}
};
///////////////////////////////////////////////////////////////////////////////

jint JNI_OnLoad(JavaVM* vm, void* reserved)
{
	JNIEnv* env = NULL;
	jclass clazz;
	int methodsLenght;

	if (vm->GetEnv((void**)&env, JNI_VERSION_1_4) != JNI_OK)
		return JNI_ERR;

	assert(env != NULL);

	clazz = env->FindClass(gClassName);
	if (clazz == NULL)
		return JNI_ERR;

	methodsLenght = sizeof(gExportMethods) / sizeof(gExportMethods[0]);
	if (env->RegisterNatives(clazz, gExportMethods, methodsLenght) < 0)
		return JNI_ERR;

	return JNI_VERSION_1_4;
}

void JNI_OnUnload(JavaVM* vm, void* reserved)
{
}

speed_t getBaudrate(jint baudrate) {
    switch (baudrate) {
    case 0:
        return B0;
    case 50:
        return B50;
    case 75:
        return B75;
    case 110:
        return B110;
    case 134:
        return B134;
    case 150:
        return B150;
    case 200:
        return B200;
    case 300:
        return B300;
    case 600:
        return B600;
    case 1200:
        return B1200;
    case 1800:
        return B1800;
    case 2400:
        return B2400;
    case 4800:
        return B4800;
    case 9600:
        return B9600;
    case 19200:
        return B19200;
    case 38400:
        return B38400;
    case 57600:
        return B57600;
    case 115200:
        return B115200;
    case 230400:
        return B230400;
    case 460800:
        return B460800;
    case 500000:
        return B500000;
    case 576000:
        return B576000;
    case 921600:
        return B921600;
    case 1000000:
        return B1000000;
    case 1152000:
        return B1152000;
    case 1500000:
        return B1500000;
    case 2000000:
        return B2000000;
    case 2500000:
        return B2500000;
    case 3000000:
        return B3000000;
    case 3500000:
        return B3500000;
    case 4000000:
        return B4000000;
    default:
        return -1;
    }
}

JNIEXPORT jobject JNICALL com_open(JNIEnv *env, jclass clazz, jstring path,jint baudrate,
		jint dataBit, jint parity, jint stopBits, jint dataFlow)
{
	int fd;
	speed_t speed;
	jobject fileDescriptor;

	// LOGD("init native Check arguments");
	/* Check arguments */
	{
		speed = getBaudrate(baudrate);
		if (speed == -1) {
			// LOGE("Invalid baudrate");
			return NULL;
		}
	}

	//LOGD("init native Opening device!");
	/* Opening device */
	{
		jboolean iscopy;
		const char *path_utf = env->GetStringUTFChars(path, &iscopy);
		// LOGD("Opening serial port %s", path_utf);
		// fd = open(path_utf, O_RDWR | O_DIRECT | O_SYNC);
		fd = open(path_utf, O_RDWR | O_NOCTTY | O_NONBLOCK | O_NDELAY);
		// LOGD("open() fd = %d", fd);
		env->ReleaseStringUTFChars(path, path_utf);
		if (fd == -1) {
			// LOGE("Cannot open port %d",baudrate);
			return NULL;
		}
	}

	// LOGD("init native Configure device!");
	/* Configure device */
	//*/
	{
		struct termios cfg;
		if (tcgetattr(fd, &cfg)) {
			// LOGE("Configure device tcgetattr() failed 1");
			close(fd);
			return NULL;
		}

		// LOGD("########: [C=%08o][I=%08o][O=%08o][L=%08o]",
		//		cfg.c_cflag, cfg.c_iflag, cfg.c_oflag, cfg.c_lflag);

		// cfmakeraw(&cfg);
		cfsetispeed(&cfg, speed);
		cfsetospeed(&cfg, speed);

		cfg.c_cflag |= CSIZE;
		cfg.c_cflag |= CS8;
		cfg.c_cflag &= ~PARENB;
		cfg.c_cflag &= ~CSTOPB;
		cfg.c_cflag &= ~CRTSCTS;
		cfg.c_iflag &= ~(IXON|IXOFF|IXANY);
		cfg.c_iflag |= IGNCR;
		cfg.c_iflag &= ~(INLCR|ICRNL);
		cfg.c_lflag &= ~(ICANON|ECHO|ECHOE|ECHOK|ECHONL|NOFLSH);


		// LOGD("###==###: [C=%08o][I=%08o][O=%08o][L=%08o]",
		//		cfg.c_cflag, cfg.c_iflag, cfg.c_oflag, cfg.c_lflag);

		if (tcsetattr(fd, TCSANOW, &cfg)) {
			// LOGE("Configure device tcsetattr() failed 2");
			close(fd);
			return NULL;
		}
	}//**/

	/* Create a corresponding file descriptor */
	{
		jclass cFileDescriptor = env->FindClass("java/io/FileDescriptor");
		jmethodID iFileDescriptor = env->GetMethodID(cFileDescriptor,"<init>", "()V");
		jfieldID descriptorID = env->GetFieldID(cFileDescriptor,"descriptor", "I");
		fileDescriptor = env->NewObject(cFileDescriptor,iFileDescriptor);
		env->SetIntField(fileDescriptor, descriptorID, (jint) fd);
	}

	return fileDescriptor;
}

JNIEXPORT jint JNICALL com_close(JNIEnv * env, jclass clazz, jobject fileDescriptor)
{
	if (clazz == NULL || fileDescriptor == NULL)
	{
		return -1;
	}

    jclass FileDescriptorClazz = env->GetObjectClass(fileDescriptor);
    jfieldID descriptorID = env->GetFieldID(FileDescriptorClazz, "descriptor", "I");
    jint descriptor = env->GetIntField(fileDescriptor, descriptorID);

    //LOGD("close(fd = %d)", descriptor);
    close(descriptor);
    return 1;
}

