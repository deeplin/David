LOCAL_PATH := $(call my-dir)

################################################################################

include $(CLEAR_VARS)

LOCAL_MODULE    := serialport
LOCAL_SRC_FILES := serialport.cpp

# LOCAL_LDLIBS    := -llog 

include $(BUILD_SHARED_LIBRARY)

################################################################################

include $(CLEAR_VARS)

LOCAL_MODULE    := watchdog
LOCAL_SRC_FILES := watchdog.cpp

LOCAL_LDLIBS    := -llog 

include $(BUILD_SHARED_LIBRARY)

################################################################################