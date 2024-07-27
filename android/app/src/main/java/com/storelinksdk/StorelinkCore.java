import android.content.Context;
import androidx.annotation.Nullable;

public class StorelinkCore {

    // Configuration for the SDK
    public static class Configuration {
        private String refreshToken;
        private LogLevel logLevel;
        private OnConfigurationSuccessListener onConfigurationSuccess;
        private OnStoreConnectionEventListener onStoreConnectionEvent;
        private OnInvoiceEventListener onInvoiceEvent;
        private OnCheckingStoreConnectionEventListener onCheckingStoreConnectionEvent;
        private String brandName;
        private String logoUrl;
        private String devApiLocation;

        public Configuration(String refreshToken) {
            this.refreshToken = refreshToken;
        }

        public Configuration setLogLevel(LogLevel logLevel) {
            this.logLevel = logLevel;
            return this;
        }

        public Configuration setOnConfigurationSuccess(OnConfigurationSuccessListener listener) {
            this.onConfigurationSuccess = listener;
            return this;
        }

        public Configuration setOnStoreConnectionEvent(OnStoreConnectionEventListener listener) {
            this.onStoreConnectionEvent = listener;
            return this;
        }

        public Configuration setOnInvoiceEvent(OnInvoiceEventListener listener) {
            this.onInvoiceEvent = listener;
            return this;
        }

        public Configuration setOnCheckingStoreConnectionEvent(OnCheckingStoreConnectionEventListener listener) {
            this.onCheckingStoreConnectionEvent = listener;
            return this;
        }

        public Configuration setBrandName(String brandName) {
            this.brandName = brandName;
            return this;
        }

        public Configuration setLogoUrl(String logoUrl) {
            this.logoUrl = logoUrl;
            return this;
        }

        public Configuration setDevApiLocation(String devApiLocation) {
            this.devApiLocation = devApiLocation;
            return this;
        }
    }

    // Possible errors for the SDK
    public enum SDKError {
        INVALID_REFRESH_TOKEN,
        UNKNOWN_ERROR,
        NATIVE_COMMUNICATION_ERROR
    }

    public enum PresentationMethod {
        PRESENT_MODALLY,
        PUSH_ON_NAVIGATION_STACK,
        EMBED_IN_TAB
    }

    // Listener interfaces
    public interface OnConfigurationSuccessListener {
        void onConfigurationSuccess(Map<String, Object> data);
    }

    public interface OnStoreConnectionEventListener {
        void onStoreConnectionEvent(Map<String, Object> data);
    }

    public interface OnInvoiceEventListener {
        void onInvoiceEvent(Map<String, Object> data);
    }

    public interface OnCheckingStoreConnectionEventListener {
        void onCheckingStoreConnectionEvent(Map<String, Object> data);
    }
}
