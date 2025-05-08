# Profiling


## بررسی کلاس JavaCup - قسمت اول



ابتدا کلاس JavaCup را با استفاده از ابزار Yourkit مانیتور کردیم. نتایج در عکس‌های زیر آمده است.

![profiling initial run](https://github.com/user-attachments/assets/b4aca77e-0843-42a9-97e4-08976da13f1c)
![profiling yourkit 1 1](https://github.com/user-attachments/assets/cde50210-be4d-45aa-ac91-5147c062bff2)


همانطور که مشاهده می‌کنید، بعد از دادن ۳ عدد ورودی، مصرف منابع به صورت صعودی رشد می‌کند و بعد از مدتی خطای heap داده می‌شود.


طمابق کد می‌توان متوجه شد که اکثر استفاده‌ی کدی که از منابع است، تیکه کد تابع `temp()` می‌باشد. حال ما قصد داریم پیاده‌سازی این تابع را به نحوی عوض کنیم که نسبت به قبل بهتر شود.

کد قبل:

```
   public static void temp() {
        ArrayList a = new ArrayList();
        for (int i = 0; i < 10000; i++)
        {
            for (int j = 0; j < 20000; j++) {
                a.add(i + j);
            }
        }
}
```


کد بعد:

```
 public static void temp() {
        int[] a = new int[10000 * 20000];
        int cpunter = 0;
        for (int i = 0; i < 10000; i++) {
            for (int j = 0; j < 20000; j++) {
                a[cpunter] = i + j;
                cpunter += 1;
            }
        }
    }
```

با تغییر دادن ArrayList به یک آرایه‌ی استاتیک، خطایی که قبلا بخاطر  heap می‌خوردیم را دیگر نمی‌خوریم. همچنین بر خلاف کد قبل، به خروجی نیز دست پیدا می‌کنیم.

![image](https://github.com/user-attachments/assets/e450b940-d990-4bcc-8973-7abbf3b766a6)



مطابق عکس‌های زیر، profiling کد جدید بعد از تغییر تابع temp به شکل زیر می‌باشد:

![image](https://github.com/user-attachments/assets/2946ec92-bd62-4581-9e91-c6026d2d88ac)


## قسمت دوم 


