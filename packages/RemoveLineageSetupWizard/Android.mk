LOCAL_PATH := $(call my-dir)

# GMS builds: Google's setup wizard replaces Lineage's (both at once make
# the user choose between two setup apps)
include $(CLEAR_VARS)
LOCAL_MODULE := RemoveLineageSetupWizard
LOCAL_MODULE_CLASS := APPS
LOCAL_MODULE_TAGS := optional
LOCAL_OVERRIDES_PACKAGES := LineageSetupWizard
LOCAL_UNINSTALLABLE_MODULE := true
LOCAL_CERTIFICATE := PRESIGNED
LOCAL_SRC_FILES := /dev/null
include $(BUILD_PREBUILT)
