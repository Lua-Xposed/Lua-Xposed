TOP_PATH := $(call my-dir)

# lua
include $(CLEAR_VARS)

LOCAL_PATH := $(TOP_PATH)/../../lua
LOCAL_MODULE := lua
LOCAL_SRC_FILES := $(subst $(LOCAL_PATH)/,,$(wildcard $(LOCAL_PATH)/*.c))

include $(BUILD_STATIC_LIBRARY)


# luasocket
include $(CLEAR_VARS)

LOCAL_PATH := $(TOP_PATH)/../../luasocket/src
LOCAL_MODULE := luasocket
LOCAL_C_INCLUDES += $(TOP_PATH)/../../lua
LOCAL_SRC_FILES := auxiliar.c buffer.c except.c inet.c io.c luasocket.c mime.c options.c select.c tcp.c timeout.c udp.c usocket.c

include $(BUILD_STATIC_LIBRARY)


# binding
include $(CLEAR_VARS)

LOCAL_PATH := $(TOP_PATH)
LOCAL_MODULE := luabinding
LOCAL_C_INCLUDES += $(TOP_PATH)/../../lua/
LOCAL_C_INCLUDES += $(TOP_PATH)/../../luasocket/src/
LOCAL_SRC_FILES := $(subst $(LOCAL_PATH)/,,$(wildcard $(LOCAL_PATH)/*.c))
LOCAL_STATIC_LIBRARIES := libluabinding

include $(BUILD_SHARED_LIBRARY)