import { useState } from 'react'

function App() {
  const [activeTab, setActiveTab] = useState<'overview' | 'structure' | 'build' | 'features'>('overview')

  const tabs = [
    { id: 'overview' as const, label: 'معرفی' },
    { id: 'features' as const, label: 'ویژگی‌ها' },
    { id: 'structure' as const, label: 'ساختار پروژه' },
    { id: 'build' as const, label: 'نحوه ساخت APK' },
  ]

  return (
    <div className="min-h-screen bg-gradient-to-br from-slate-900 via-purple-900 to-slate-900 text-white" dir="rtl">
      {/* Hero Section */}
      <div className="relative overflow-hidden">
        <div className="absolute inset-0 bg-[url('data:image/svg+xml;base64,PHN2ZyB3aWR0aD0iNjAiIGhlaWdodD0iNjAiIHZpZXdCb3g9IjAgMCA2MCA2MCIgeG1sbnM9Imh0dHA6Ly93d3cudzMub3JnLzIwMDAvc3ZnIj48ZyBmaWxsPSJub25lIiBmaWxsLXJ1bGU9ImV2ZW5vZGQiPjxnIGZpbGw9IiNmZmYiIGZpbGwtb3BhY2l0eT0iMC4wMyI+PHBhdGggZD0iTTM2IDM0djItSDI0di0yaDEyem0wLTMwVjBoLTJ2NEgyNFYwSDEydjRIMFY2aDEyVjRoMTJWMGgxMlY0aDEyVjZINjBWNEg0OFYwSDM2djRIMjRWMEgxMnoiLz48L2c+PC9nPjwvc3ZnPg==')] opacity-20"></div>
        
        <div className="max-w-6xl mx-auto px-4 py-16 relative z-10">
          <div className="text-center mb-12">
            {/* App Icon */}
            <div className="inline-flex items-center justify-center w-24 h-24 bg-gradient-to-br from-purple-500 to-indigo-600 rounded-3xl shadow-2xl shadow-purple-500/30 mb-8">
              <svg className="w-14 h-14 text-white" fill="none" viewBox="0 0 24 24" stroke="currentColor" strokeWidth={1.5}>
                <path strokeLinecap="round" strokeLinejoin="round" d="M2.25 18.75a60.07 60.07 0 0115.797 2.101c.727.198 1.453-.342 1.453-1.096V18.75M3.75 4.5v.75A.75.75 0 013 6h-.75m0 0v-.375c0-.621.504-1.125 1.125-1.125H20.25M2.25 6v9m18-10.5v.75c0 .414.336.75.75.75h.75m-1.5-1.5h.375c.621 0 1.125.504 1.125 1.125v9.75c0 .621-.504 1.125-1.125 1.125h-.375m1.5-1.5H21a.75.75 0 00-.75.75v.75m0 0H3.75m0 0h-.375a1.125 1.125 0 01-1.125-1.125V15m1.5 1.5v-.75A.75.75 0 003 15h-.75M15 10.5a3 3 0 11-6 0 3 3 0 016 0zm3 0h.008v.008H18V10.5zm-12 0h.008v.008H6V10.5z" />
              </svg>
            </div>
            
            <h1 className="text-5xl font-black mb-4 bg-gradient-to-l from-purple-300 to-white bg-clip-text text-transparent">
              مدیریت اقساط
            </h1>
            <p className="text-xl text-purple-200 mb-2">Installment Manager</p>
            <p className="text-lg text-gray-300 max-w-2xl mx-auto">
              اپلیکیشن بومی اندروید برای مدیریت اقساط و پرداخت‌ها
            </p>
            
            {/* Badge */}
            <div className="flex items-center justify-center gap-3 mt-8 flex-wrap">
              <span className="inline-flex items-center gap-1.5 px-4 py-2 bg-green-500/20 border border-green-500/30 rounded-full text-green-300 text-sm">
                <span className="w-2 h-2 bg-green-400 rounded-full animate-pulse"></span>
                Native Android (Kotlin + Jetpack Compose)
              </span>
              <span className="inline-flex items-center gap-1.5 px-4 py-2 bg-blue-500/20 border border-blue-500/30 rounded-full text-blue-300 text-sm">
                🔒 کاملاً آفلاین
              </span>
              <span className="inline-flex items-center gap-1.5 px-4 py-2 bg-purple-500/20 border border-purple-500/30 rounded-full text-purple-300 text-sm">
                🇮🇷 فارسی / RTL
              </span>
            </div>
          </div>

          {/* Phone Mockup */}
          <div className="flex justify-center mb-12">
            <div className="relative">
              <div className="w-72 h-[520px] bg-gray-900 rounded-[2.5rem] border-4 border-gray-700 shadow-2xl shadow-black/50 overflow-hidden">
                {/* Status bar */}
                <div className="h-8 bg-gray-800 flex items-center justify-between px-6">
                  <span className="text-xs text-gray-400">۱۲:۳۰</span>
                  <div className="flex gap-1">
                    <div className="w-3 h-3 bg-gray-600 rounded-sm"></div>
                    <div className="w-3 h-3 bg-gray-600 rounded-sm"></div>
                  </div>
                </div>
                
                {/* App content mockup */}
                <div className="p-4 space-y-3" dir="rtl">
                  <div className="flex justify-between items-center">
                    <div>
                      <h3 className="text-white font-bold text-sm">مدیریت اقساط</h3>
                      <p className="text-gray-400 text-xs">۱۵ اردیبهشت ۱۴۰۴</p>
                    </div>
                    <div className="flex gap-2">
                      <div className="w-6 h-6 bg-gray-700 rounded-md"></div>
                      <div className="w-6 h-6 bg-gray-700 rounded-md"></div>
                    </div>
                  </div>
                  
                  <div className="grid grid-cols-2 gap-2">
                    <div className="bg-purple-900/50 rounded-xl p-3">
                      <div className="text-purple-300 text-xs">اقساط فعال</div>
                      <div className="text-white font-bold text-lg">۳</div>
                    </div>
                    <div className="bg-red-900/30 rounded-xl p-3">
                      <div className="text-red-300 text-xs">معوقه</div>
                      <div className="text-white font-bold text-lg">۱</div>
                    </div>
                    <div className="bg-amber-900/30 rounded-xl p-3">
                      <div className="text-amber-300 text-xs">مانده کل</div>
                      <div className="text-amber-200 font-bold text-xs">۴۵,۰۰۰,۰۰۰ ت</div>
                    </div>
                    <div className="bg-green-900/30 rounded-xl p-3">
                      <div className="text-green-300 text-xs">پرداخت شده</div>
                      <div className="text-green-200 font-bold text-xs">۱۵,۰۰۰,۰۰۰ ت</div>
                    </div>
                  </div>
                  
                  {/* Installment Cards */}
                  <div className="bg-gray-800 rounded-xl p-3 space-y-2">
                    <div className="flex items-center gap-2">
                      <div className="w-8 h-8 bg-green-800 rounded-full flex items-center justify-center text-xs">🚗</div>
                      <div className="flex-1">
                        <div className="text-white text-xs font-bold">وام خودرو</div>
                        <div className="text-green-400 text-[10px]">خودرو</div>
                      </div>
                    </div>
                    <div className="w-full bg-gray-700 rounded-full h-1.5">
                      <div className="bg-green-500 h-1.5 rounded-full" style={{width: '60%'}}></div>
                    </div>
                    <div className="flex justify-between text-[10px] text-gray-400">
                      <span>قسط ۱۸ از ۳۰</span>
                      <span>۶۰٪</span>
                    </div>
                  </div>
                  
                  <div className="bg-gray-800 rounded-xl p-3 space-y-2">
                    <div className="flex items-center gap-2">
                      <div className="w-8 h-8 bg-blue-800 rounded-full flex items-center justify-center text-xs">🏠</div>
                      <div className="flex-1">
                        <div className="text-white text-xs font-bold">وام مسکن</div>
                        <div className="text-blue-400 text-[10px]">مسکن</div>
                      </div>
                    </div>
                    <div className="w-full bg-gray-700 rounded-full h-1.5">
                      <div className="bg-blue-500 h-1.5 rounded-full" style={{width: '25%'}}></div>
                    </div>
                    <div className="flex justify-between text-[10px] text-gray-400">
                      <span>قسط ۶ از ۲۴</span>
                      <span>۲۵٪</span>
                    </div>
                  </div>

                  <div className="bg-red-900/20 border border-red-800/30 rounded-xl p-3 space-y-2">
                    <div className="flex items-center gap-2">
                      <div className="w-8 h-8 bg-purple-800 rounded-full flex items-center justify-center text-xs">📱</div>
                      <div className="flex-1">
                        <div className="text-white text-xs font-bold">قسط موبایل</div>
                        <div className="text-purple-400 text-[10px]">موبایل</div>
                      </div>
                      <span className="text-red-400 text-[10px]">⚠</span>
                    </div>
                    <div className="w-full bg-gray-700 rounded-full h-1.5">
                      <div className="bg-purple-500 h-1.5 rounded-full" style={{width: '40%'}}></div>
                    </div>
                  </div>
                </div>
                
                {/* FAB */}
                <div className="absolute bottom-6 left-4">
                  <div className="bg-purple-600 text-white px-4 py-2 rounded-2xl text-xs font-bold shadow-lg flex items-center gap-1">
                    + قسط جدید
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>

      {/* Tabs Section */}
      <div className="max-w-5xl mx-auto px-4 pb-20">
        <div className="flex gap-2 mb-8 overflow-x-auto">
          {tabs.map(tab => (
            <button
              key={tab.id}
              onClick={() => setActiveTab(tab.id)}
              className={`px-6 py-3 rounded-xl text-sm font-medium whitespace-nowrap transition-all ${
                activeTab === tab.id
                  ? 'bg-purple-600 text-white shadow-lg shadow-purple-600/30'
                  : 'bg-white/5 text-gray-300 hover:bg-white/10'
              }`}
            >
              {tab.label}
            </button>
          ))}
        </div>

        {/* Overview Tab */}
        {activeTab === 'overview' && (
          <div className="space-y-6">
            <div className="bg-white/5 backdrop-blur-lg rounded-2xl p-8 border border-white/10">
              <h2 className="text-2xl font-bold mb-4 text-purple-300">📱 اپلیکیشن بومی اندروید</h2>
              <p className="text-gray-300 leading-8 text-lg">
                این پروژه یک اپلیکیشن <strong className="text-white">بومی اندروید</strong> است که با 
                <strong className="text-green-300"> Kotlin</strong> و 
                <strong className="text-blue-300"> Jetpack Compose</strong> نوشته شده است.
                این یک وب‌سایت نیست — یک اپلیکیشن واقعی اندروید است که مستقیماً روی گوشی شما نصب می‌شود.
              </p>
            </div>

            <div className="grid md:grid-cols-2 gap-4">
              <TechCard 
                title="Kotlin" 
                version="1.9.22" 
                desc="زبان برنامه‌نویسی اصلی اندروید"
                color="purple"
              />
              <TechCard 
                title="Jetpack Compose" 
                version="BOM 2024.01" 
                desc="رابط کاربری مدرن اندروید"
                color="blue"
              />
              <TechCard 
                title="Material 3" 
                version="Latest" 
                desc="سیستم طراحی گوگل"
                color="green"
              />
              <TechCard 
                title="Room Database" 
                version="2.6.1" 
                desc="ذخیره‌سازی محلی داده"
                color="amber"
              />
              <TechCard 
                title="Navigation Compose" 
                version="2.7.6" 
                desc="ناوبری بین صفحات"
                color="cyan"
              />
              <TechCard 
                title="AlarmManager" 
                version="Native" 
                desc="زمان‌بندی یادآوری پرداخت"
                color="red"
              />
            </div>
          </div>
        )}

        {/* Features Tab */}
        {activeTab === 'features' && (
          <div className="space-y-4">
            <FeatureCard
              emoji="📋"
              title="مدیریت نامحدود اقساط"
              desc="افزودن، ویرایش و حذف اقساط با جزئیات کامل شامل عنوان، مبلغ، تعداد و تاریخ شروع"
            />
            <FeatureCard
              emoji="📂"
              title="دسته‌بندی‌های متنوع"
              desc="خودرو، مسکن، موبایل، لوازم خانگی، تحصیلی، پزشکی، شخصی، وام بانکی، خرید و سایر"
            />
            <FeatureCard
              emoji="📅"
              title="تقویم شمسی"
              desc="تقویم کامل جلالی/شمسی با نمایش اقساط هر روز و ماه"
            />
            <FeatureCard
              emoji="🔔"
              title="یادآوری پرداخت"
              desc="نوتیفیکیشن اندروید ۳ روز قبل از سررسید هر قسط با قابلیت تنظیم"
            />
            <FeatureCard
              emoji="📊"
              title="آمار و گزارش"
              desc="نمودار دایره‌ای پیشرفت، تفکیک بر اساس دسته‌بندی، مقایسه پرداخت شده و مانده"
            />
            <FeatureCard
              emoji="🌙"
              title="حالت تاریک/روشن"
              desc="تم تاریک و روشن Material 3 با قابلیت تغییر از تنظیمات"
            />
            <FeatureCard
              emoji="💰"
              title="واحد پول تومان/ریال"
              desc="امکان تغییر واحد نمایش مبالغ بین تومان و ریال"
            />
            <FeatureCard
              emoji="📴"
              title="کاملاً آفلاین"
              desc="تمام داده‌ها روی دستگاه ذخیره می‌شود - بدون نیاز به اینترنت"
            />
            <FeatureCard
              emoji="🔄"
              title="بازیابی یادآوری پس از ریستارت"
              desc="Boot Receiver برای بازیابی خودکار تمام یادآوری‌ها پس از ریستارت گوشی"
            />
          </div>
        )}

        {/* Structure Tab */}
        {activeTab === 'structure' && (
          <div className="bg-white/5 backdrop-blur-lg rounded-2xl p-8 border border-white/10">
            <h2 className="text-xl font-bold mb-6 text-purple-300">ساختار پروژه</h2>
            <pre className="text-sm text-gray-300 leading-7 overflow-x-auto font-mono" dir="ltr">{`android-app/
├── app/
│   ├── src/main/
│   │   ├── AndroidManifest.xml
│   │   ├── java/com/installment/manager/
│   │   │   ├── InstallmentApp.kt           # Application class
│   │   │   ├── MainActivity.kt             # Main Activity  
│   │   │   ├── data/
│   │   │   │   ├── AppDatabase.kt          # Room Database
│   │   │   │   ├── dao/
│   │   │   │   │   ├── InstallmentDao.kt   # Installment queries
│   │   │   │   │   └── PaymentDao.kt       # Payment queries
│   │   │   │   └── model/
│   │   │   │       ├── Installment.kt      # Installment entity
│   │   │   │       ├── Payment.kt          # Payment entity
│   │   │   │       └── Category.kt         # Default categories
│   │   │   ├── notification/
│   │   │   │   ├── NotificationScheduler.kt  # AlarmManager scheduling
│   │   │   │   ├── PaymentAlarmReceiver.kt   # Alarm handler
│   │   │   │   └── BootReceiver.kt           # Reschedule on boot
│   │   │   ├── ui/
│   │   │   │   ├── navigation/
│   │   │   │   │   ├── NavRoutes.kt        # Route definitions
│   │   │   │   │   └── AppNavigation.kt    # NavHost setup
│   │   │   │   ├── screens/
│   │   │   │   │   ├── HomeScreen.kt       # Main dashboard
│   │   │   │   │   ├── AddInstallmentScreen.kt
│   │   │   │   │   ├── InstallmentDetailScreen.kt
│   │   │   │   │   ├── CalendarScreen.kt   # Persian calendar
│   │   │   │   │   ├── StatisticsScreen.kt # Charts & stats
│   │   │   │   │   └── SettingsScreen.kt   # App settings
│   │   │   │   └── theme/
│   │   │   │       ├── Theme.kt            # Material 3 theme
│   │   │   │       └── Type.kt             # Typography
│   │   │   ├── util/
│   │   │   │   ├── PersianDateUtil.kt      # Jalali converter
│   │   │   │   └── PreferencesManager.kt   # DataStore prefs
│   │   │   └── viewmodel/
│   │   │       └── MainViewModel.kt        # Business logic
│   │   └── res/
│   │       ├── drawable/                   # Icons
│   │       ├── mipmap-hdpi/               # App icons
│   │       └── values/                    # Strings, colors, themes
│   └── build.gradle.kts                   # App-level build config
├── build.gradle.kts                       # Project-level build config
├── settings.gradle.kts                    # Project settings
├── gradle.properties                      # Gradle configuration
└── gradle/wrapper/
    └── gradle-wrapper.properties          # Gradle wrapper version`}</pre>
          </div>
        )}

        {/* Build Tab */}
        {activeTab === 'build' && (
          <div className="space-y-6">
            <div className="bg-amber-500/10 border border-amber-500/30 rounded-2xl p-6">
              <h3 className="text-amber-300 font-bold text-lg mb-2">⚠️ توجه مهم</h3>
              <p className="text-amber-200/80">
                این محیط (وب) قابلیت کامپایل پروژه اندروید را ندارد.
                تمام فایل‌های پروژه در پوشه <code className="bg-amber-500/20 px-2 py-0.5 rounded">android-app/</code> آماده است.
                فقط کافیست آن را در Android Studio باز کنید.
              </p>
            </div>

            <BuildStep
              number={1}
              title="دانلود پروژه"
              desc="تمام محتوای پوشه android-app/ را دانلود کنید"
            />
            <BuildStep
              number={2}
              title="باز کردن در Android Studio"
              desc='Android Studio را باز کنید → File → Open → پوشه android-app را انتخاب کنید'
            />
            <BuildStep
              number={3}
              title="Gradle Sync"
              desc="منتظر بمانید تا Gradle تمام dependency ها را دانلود و sync کند"
            />
            <BuildStep
              number={4}
              title="ساخت APK"
              desc='از منو: Build → Build Bundle(s) / APK(s) → Build APK(s) یا از ترمینال: ./gradlew assembleDebug'
            />
            <BuildStep
              number={5}
              title="نصب روی گوشی"
              desc="APK خروجی در مسیر app/build/outputs/apk/debug/ قرار دارد. آن را به گوشی منتقل و نصب کنید."
            />

            <div className="bg-white/5 backdrop-blur-lg rounded-2xl p-6 border border-white/10">
              <h3 className="text-lg font-bold mb-4 text-purple-300">🖥️ دستور ساخت از ترمینال</h3>
              <pre className="bg-black/50 rounded-xl p-4 text-green-300 font-mono text-sm overflow-x-auto" dir="ltr">{`# Debug build
cd android-app
./gradlew assembleDebug

# Release build (needs signing key)
./gradlew assembleRelease

# Install directly on connected device
./gradlew installDebug`}</pre>
            </div>

            <div className="bg-white/5 backdrop-blur-lg rounded-2xl p-6 border border-white/10">
              <h3 className="text-lg font-bold mb-4 text-purple-300">📋 پیش‌نیازها</h3>
              <ul className="space-y-2 text-gray-300">
                <li className="flex items-center gap-2">
                  <span className="text-green-400">✓</span>
                  Android Studio Hedgehog (2023.1.1) یا بالاتر
                </li>
                <li className="flex items-center gap-2">
                  <span className="text-green-400">✓</span>
                  JDK 17
                </li>
                <li className="flex items-center gap-2">
                  <span className="text-green-400">✓</span>
                  Android SDK 34
                </li>
                <li className="flex items-center gap-2">
                  <span className="text-green-400">✓</span>
                  حداقل ۴ گیگابایت فضای آزاد
                </li>
              </ul>
            </div>
          </div>
        )}

        {/* Footer */}
        <div className="mt-16 text-center text-gray-500 text-sm border-t border-white/5 pt-8">
          <p>
            پروژه بومی اندروید — Kotlin • Jetpack Compose • Material 3 • Room DB
          </p>
          <p className="mt-2">
            تمام فایل‌های سورس در پوشه <code className="bg-white/5 px-2 py-0.5 rounded">android-app/</code> موجود است
          </p>
        </div>
      </div>
    </div>
  )
}

function TechCard({ title, version, desc, color }: { title: string; version: string; desc: string; color: string }) {
  const colorMap: Record<string, string> = {
    purple: 'from-purple-500/20 to-purple-600/5 border-purple-500/20 text-purple-300',
    blue: 'from-blue-500/20 to-blue-600/5 border-blue-500/20 text-blue-300',
    green: 'from-green-500/20 to-green-600/5 border-green-500/20 text-green-300',
    amber: 'from-amber-500/20 to-amber-600/5 border-amber-500/20 text-amber-300',
    cyan: 'from-cyan-500/20 to-cyan-600/5 border-cyan-500/20 text-cyan-300',
    red: 'from-red-500/20 to-red-600/5 border-red-500/20 text-red-300',
  }

  return (
    <div className={`bg-gradient-to-br ${colorMap[color]} border rounded-xl p-5`}>
      <div className="flex items-center justify-between mb-2">
        <h3 className="font-bold text-white">{title}</h3>
        <span className={`text-xs px-2 py-0.5 rounded-full bg-white/10 ${colorMap[color]?.split(' ').pop()}`}>
          {version}
        </span>
      </div>
      <p className="text-gray-400 text-sm">{desc}</p>
    </div>
  )
}

function FeatureCard({ emoji, title, desc }: { emoji: string; title: string; desc: string }) {
  return (
    <div className="bg-white/5 backdrop-blur-lg rounded-xl p-5 border border-white/10 flex gap-4 items-start hover:bg-white/10 transition-colors">
      <span className="text-3xl">{emoji}</span>
      <div>
        <h3 className="font-bold text-white mb-1">{title}</h3>
        <p className="text-gray-400 text-sm">{desc}</p>
      </div>
    </div>
  )
}

function BuildStep({ number, title, desc }: { number: number; title: string; desc: string }) {
  const persianNumbers = ['۱', '۲', '۳', '۴', '۵']
  return (
    <div className="bg-white/5 backdrop-blur-lg rounded-xl p-5 border border-white/10 flex gap-4 items-start">
      <div className="w-10 h-10 shrink-0 bg-purple-600 rounded-xl flex items-center justify-center font-bold text-lg">
        {persianNumbers[number - 1]}
      </div>
      <div>
        <h3 className="font-bold text-white mb-1">{title}</h3>
        <p className="text-gray-400 text-sm">{desc}</p>
      </div>
    </div>
  )
}

export default App
