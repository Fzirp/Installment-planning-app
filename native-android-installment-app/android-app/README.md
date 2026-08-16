# مدیریت اقساط (Installment Manager)

یک اپلیکیشن اندرویدی بومی برای مدیریت اقساط و پرداخت‌ها با پشتیبانی کامل از زبان فارسی و تقویم شمسی.

## ویژگی‌ها

- ✅ مدیریت نامحدود اقساط
- ✅ دسته‌بندی (خودرو، مسکن، موبایل، لوازم خانگی، تحصیلی، پزشکی و...)
- ✅ تقویم شمسی کامل
- ✅ یادآوری پرداخت (۳ روز قبل از سررسید)
- ✅ آمار و نمودار پیشرفت
- ✅ حالت تاریک/روشن
- ✅ واحد پول تومان/ریال
- ✅ کاملاً آفلاین - بدون نیاز به اینترنت
- ✅ پشتیبانی کامل RTL فارسی
- ✅ Material 3 Design

## پیش‌نیازها

- Android Studio Hedgehog (2023.1.1) یا بالاتر
- JDK 17
- Android SDK 34
- Kotlin 1.9.22

## نحوه ساخت APK

### روش ۱: از Android Studio

1. پوشه `android-app` را در Android Studio باز کنید
2. منتظر بمانید تا Gradle sync تمام شود
3. از منو: `Build → Build Bundle(s) / APK(s) → Build APK(s)`
4. فایل APK در مسیر `app/build/outputs/apk/debug/app-debug.apk` ایجاد می‌شود

### روش ۲: از Command Line

```bash
cd android-app
./gradlew assembleDebug
```

فایل APK در `app/build/outputs/apk/debug/app-debug.apk` قرار می‌گیرد.

### ساخت نسخه Release

```bash
cd android-app
./gradlew assembleRelease
```

**توجه:** برای نسخه Release نیاز به signing key دارید.

## نصب روی گوشی

1. فایل APK را به گوشی منتقل کنید
2. در تنظیمات گوشی، نصب از منابع ناشناس را فعال کنید
3. فایل APK را اجرا و نصب کنید

## ساختار پروژه

```
android-app/
├── app/
│   ├── src/main/
│   │   ├── AndroidManifest.xml
│   │   ├── java/com/installment/manager/
│   │   │   ├── InstallmentApp.kt          # Application class
│   │   │   ├── MainActivity.kt            # Main Activity
│   │   │   ├── data/
│   │   │   │   ├── AppDatabase.kt         # Room Database
│   │   │   │   ├── dao/
│   │   │   │   │   ├── InstallmentDao.kt  # Installment DAO
│   │   │   │   │   └── PaymentDao.kt      # Payment DAO
│   │   │   │   └── model/
│   │   │   │       ├── Installment.kt     # Installment entity
│   │   │   │       ├── Payment.kt         # Payment entity
│   │   │   │       └── Category.kt        # Categories
│   │   │   ├── notification/
│   │   │   │   ├── NotificationScheduler.kt
│   │   │   │   ├── PaymentAlarmReceiver.kt
│   │   │   │   └── BootReceiver.kt
│   │   │   ├── ui/
│   │   │   │   ├── navigation/
│   │   │   │   │   ├── NavRoutes.kt
│   │   │   │   │   └── AppNavigation.kt
│   │   │   │   ├── screens/
│   │   │   │   │   ├── HomeScreen.kt
│   │   │   │   │   ├── AddInstallmentScreen.kt
│   │   │   │   │   ├── InstallmentDetailScreen.kt
│   │   │   │   │   ├── CalendarScreen.kt
│   │   │   │   │   ├── StatisticsScreen.kt
│   │   │   │   │   └── SettingsScreen.kt
│   │   │   │   └── theme/
│   │   │   │       ├── Theme.kt
│   │   │   │       └── Type.kt
│   │   │   ├── util/
│   │   │   │   ├── PersianDateUtil.kt
│   │   │   │   └── PreferencesManager.kt
│   │   │   └── viewmodel/
│   │   │       └── MainViewModel.kt
│   │   └── res/
│   │       ├── drawable/
│   │       ├── mipmap-hdpi/
│   │       └── values/
│   └── build.gradle.kts
├── build.gradle.kts
├── settings.gradle.kts
├── gradle.properties
└── gradle/wrapper/gradle-wrapper.properties
```

## تکنولوژی‌ها

| تکنولوژی | نسخه |
|-----------|-------|
| Kotlin | 1.9.22 |
| Jetpack Compose | BOM 2024.01.00 |
| Material 3 | Latest |
| Room Database | 2.6.1 |
| Navigation Compose | 2.7.6 |
| DataStore | 1.0.0 |
| Min SDK | 26 (Android 8.0) |
| Target SDK | 34 (Android 14) |

## مجوزهای مورد نیاز

- `POST_NOTIFICATIONS` - ارسال نوتیفیکیشن یادآوری
- `SCHEDULE_EXACT_ALARM` - زمان‌بندی دقیق یادآوری
- `RECEIVE_BOOT_COMPLETED` - بازیابی یادآوری‌ها پس از ریستارت
- `VIBRATE` - لرزش هنگام نوتیفیکیشن
- `WAKE_LOCK` - بیدار نگه داشتن دستگاه برای نوتیفیکیشن
