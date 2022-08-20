// IVoiceAssistInterface.aidl
package cn.my.hmiadapter;

// Declare any non-default types here with import statements

interface IVoiceAssistInterface {
   void changeUserName(in String name,in String form);
   String getUserName();
   boolean isFirstOpen();
   void setFirstOpen(boolean firstOpen);
   boolean isShow();
}