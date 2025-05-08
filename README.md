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


برای این قسمت، ما یک برنامه پیاده‌سازی کردیم که بر اساس آن بزرگ‌ترین عدد مربع کامل کوچک‌تر از بزرگ‌ترین مقدار مجاز int در java (یعنی 2147483647) را پیدا کنیم. برای این کار، مطابق شکل زیر، ما یک حلقه داریم و تمامی i * iها را تا قبل از این مقدار چک می‌کنیم.

```
public class PrimeSum {
    final static int MAX_INT = 2147483647;

    public static int find_the_square_root() {
        int res = -1;
        for (int i = 2; i <= MAX_INT; i++) {
            if (i * i < MAX_INT && i * i > res) {
                res = i * i;
            }
        }
        return res;
    }

    public static void main(String[] args) {
        int result = find_the_square_root();
        System.out.println(result);
    }
}
```

مطابق شکل زیر، profiling ابزار yourkit مشخص است.

![profiling with inef](https://github.com/user-attachments/assets/4ac54b48-9b97-4937-8bb8-04d026a2be4d)


مطابق کد، ما توان ۲ی اعداد را تا قبل از خود مقدار بزرگ‌ترین عدد int در جاوا ادامه می‌دهیم، در صورتی که می‌دانیم که می‌توانیم این حلقه را تا کمتر از Square بزرگ‌ترین عدد ادامه دهیم. زیرا مقدار i به صورتی که i>square(max int) باشد، قطعا i*i نیز بزرگ‌تر از max int می‌باشد. پس کد را ویرایش می‌کنیم. کد پس از ویرایش عبات است از:


```
public class PrimeSum {
    final static int MAX_INT = 2147483647;

    public static int find_the_square_root() {
        int res = -1;
        for (int i = 2; i <= Math.sqrt(MAX_INT); i++) {
            if (i * i < MAX_INT && i * i > res) {
                res = i * i;
            }
        }
        return res;
    }

    public static void main(String[] args) {
        int result = find_the_square_root();
        System.out.println(result);
    }
}
```
حال profiling را پس از اپتیمایز کردن این کد مشاهده می‌کنیم.

![profiling modified](https://github.com/user-attachments/assets/225a858d-ae5b-446c-9f91-84291933f980)


ما برای اینکه بتوانیم کد اپتیمایز شده را profile کنیم، ابتدا یک wait هم گذاشتیم. به همین دلیل است که ابتدا مقدار CPU بالا است. پس از آن زمانی که به ادامه‌ی کد می‌رسد، مقدار cpu usage به شدت کم می‌شود و خروجی کد تقریبا ۱۰ برابر سریع‌تر (از نظر زمانی) نسبت به کد قبل چاپ می‌شود.

