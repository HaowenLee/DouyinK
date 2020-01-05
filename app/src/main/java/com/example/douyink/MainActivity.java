package com.example.douyink;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.blankj.utilcode.util.EncodeUtils;
import com.blankj.utilcode.util.JsonUtils;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.douyink.image.GlideApp;
import com.github.piasy.biv.view.BigImageView;
import com.github.piasy.biv.view.GlideImageViewFactory;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.reactivex.disposables.Disposable;

public class MainActivity extends AppCompatActivity {

    private EditText editText;
    private TextView tvResult;
    private Disposable subscribe;
    private ClipboardManager myClipboard;
    private ClipData myClip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvResult = findViewById(R.id.tvResult);
        editText = findViewById(R.id.editText);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (subscribe != null && !subscribe.isDisposed()) {
            subscribe.dispose();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        String shareText = getShareText();
        if (!TextUtils.isEmpty(shareText) && shareText.contains(" https://v.douyin.com/")) {
            editText.setText(shareText);
        }
    }

    public String getShareText() {
        ClipboardManager cm = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        ClipData data = cm.getPrimaryClip();
        ClipData.Item item = data.getItemAt(0);
        String content = item.getText().toString();
        return content;
    }

    /* renamed from: b */
    private static String f15079b = "1s1z1GYRRNZRSJam";

    /* renamed from: c */
    private static String f15080c = "AES/ECB/PKCS7Padding";

    private static String h = "1Hq4yLu4yTCmTDQk8dG9e7A%2FvAZ2Q%2FRWXypJUNKnZiKlZHLc30no%2BhGCSMyKOq%2FWAdcSXJRZR6m43%2BqZncx0gEb4hAvR4uo%2FkbV4f47PDP%2BfZSdzslleObl41%2Fjb5BVbG1IKh1LppW02zXvuTXKpwxfmeBanatQzso4B866ytroR2XWBUinsHQHxlpV0MaA0gtXbvFR7hPBSHE8TioIJIsRrF8oV08PQE7RMo4RQkS2iCQG3RKaXkQrQCaFOheK6vx61FoJRXN4jrBZVOHofantZsJ9EvjbRasPLed9TL4NG9hc%2Fq%2B2Gf90iVc8U92S0DtiH17ZDhFyF2NByaBK8AnsOLDQ47epdlRQRwio4t1xszjFEptUnX0H0U2TJlFcf5cWY6GOT4IUAP%2Bl8XhTMnkktQ8qHdIj87pFxbSleYGA5O0Rcu7Lya1uyYOJ0nffgGQ9WO2mDizkNCcDPw%2Fko6%2BMJjPwr%2Feb8%2FaNImK2EYb8X0Pj58F3spYZeJ0sMbUjVazf0s9GVPKYP2zW3zZwHPiAnLEaDL9tlOMZ84jORmekyiEE40VEalyewkZkgFVdk9NzinSm2SfJJHzFN5v%2BZQXsg2jwgjQMTH5n8fUtGAK3HImywKWHff7L2QxxT5gAFzFzNxxRNdTk0BjRvXo98wvT7pXdtGkrtffnK2Fg7lfAEMAjkJ6mGO14b%2FQ0eiaTXqb0jgnVhJt6EoXusgYjDRtXDk4bVdB%2FGY5rfT3USviM84dOyhaXRMNz%2BUvt9EHRHHeG6j54%2BeaE9UNIq%2BurAxVPe1diVaXYobkzWtNIwu5pbDPKWVJLkv7vP6jPwneD9LgSTURbdFA30dSJ7Ys4DXx9mkkSrvUSuIe7SolRXnsUB9zscsP9doDjh6mDmK0ldxGODoCj4%2BQK0sigZyxe3Nfdge0qi1ZoePU%2FiFwpcm1WgKHqUjHrQJIPK%2BWOajyMPJkaRoTSjAWa7Hb%2BIez0uZqpRD%2BeQ2jpDj3Ob5%2FX9kDloBMuxAJ2qWoBYvp9STUZ%2B2OsHHLYf%2FdkodKDdn%2FLGsluLyqyg5ZTwoWcIQHXjIg5yccvEsmC1AKl4BoMIglMo%2FfiEBbyIOfaorAYGpogOYNK9jY9Q50SC21B74AUgRFyL3zkrLqxRC%2FrEOkbyPvvnOkPFn%2BdWx4iaQAbkickWVX77N98Vog7gtVWqix3rC2QHwRF2Kj%2FbcnkifDjsIZ%2BsGk9G3ha8FW7m74ez4o9kofez%2BgHWlK6KLMAEOyzB%2FPkqtUxHkYr%2FFywdOk%2FVmaJiFSRO%2FhqN3aQlEXxOS1fSMdEwAmZFd%2BMW51J%2Bk1zerOUwyc5aGt8kd%2BGGbFyXkX2rvGOl1j4fxU9bY483Q2pJdTqkbe9ZJstxynZN9YIBLR84w2Hv1WNbMOg6ohS2zr3OvDO%2BqUOwv6mSZ7pYib4%2F0qIGdGnQ7opG%2B6dsiIfkgvfH6r1N6eqJky3RKLt1OWU4qa2cPzsaq2LjahzXsi%2BSybOOK1dPhvdadYQD7YNj1VfQ1aBAhCnMmWmvsI0FB1c3K6XNKCcXmI2kHdGG4Qn58olx%2BGpXHosGojyl7D0XaejDPEzF0BbAh5J%2BX4ZYEm4xvkEeBZc37S8ku%2B6ejovBEuOIzyoyAGUj2UAPS72B6nJicVLFOBP6tD0qIaa5%2FWWuF7wOSpQucHdLWMDfkDTr4Jeh1sGmE3dzYMP3TJ5t5uWxjzQauoeSQdgd%2BIf%2F2K0p5PD5tRDgT7OIVg6XRx31PgfDhio3gU8JAh8jmjV9j4D%2B%2BncmxqKVo9IvVZTcu1zyw0KatBgUK5fA2aihK9xvwkAYTNdBf4NUOvDChleT9aLXfzrMYgPB1%2BW5L4Z1Fnm84lW9qFdg354FdjTvp0ORgm%2FRpBAVWgSb9WXgv6PJDu0d3VWBX0fqFczwVvRn3SJ4TP99u5Mm1CCXO6i%2BB3%2BTdEGp47qpB5nnbTpgB4OvYvsyGpdiO6hza6Dor0VcoP60gW1UHOW27BU8M9ynrnHKHBvzbYVrU5S7xwlSEADzGLPPzX5jyYB6Chz5FzjQATyzxHWSBDKngcR0YJJMLQipPMAPDL%2FnQT5TDzS6jrG%2BbVjg%2BmBvBaZBAkGaKr7ozXhVuQqRgPGb%2BilypQNZzBSl5ItqeNsGHaxYwr94Ywe3ANc8X0COSpojGT7lpQX%2BPf%2FHryC5dvVAtTWutp6u7Asa8QspnOo3b0KIUMU21Wu5lT1o%2FkSmZzWIJIkNvqy7moEzYEdHw%2FcAxc84gWqGcaW0yObyV9d5vFVT5OjoUcYm066UHnGFzMzLJXRb4N2OcEwSOK2ebsx7TROW9MIWJvz%2BrS5yDa7ps5EWFd7WuNgKbAO9NkLkP9satSxNQp7VCZJ7b5a0MEY%2FlxYxFckBrMOPCJHnxVRd6bYMu9nWvkqQnpUdU7zT6oEwm8g70nT59vbz1QpGfyu1olCgRPU2T9669F0jEK5lHfsQlPT3x20S58R5sSOaKkug8scKVMT%2FDzXLOZ8CoW2JmvjbN9ouicy%2Bf0Sbkg%2BKY0UJvEdrU95N71J%2BTKyI7OshaAvmjJ2QouIq5IYAJoLD5kYVROs6HJQa2vmwCc0yk%2F0l4iCSgkS3saCei91pYSa1RIzbSbDM4KBAtGosLt5IkRe8DYKPKWKDDeAvBPTPeh%2FxlmKV60R%2B1X%2FJSm0XbuWt013a3CcQxjmArG1aXWBS4fA8MOt%2FpqLe3pKPFJKgRKaRXamCFrOPO1FAOX940HDS44NpyomWsMGFj1YqKbFNg9e69JQqP%2BBE8YB%2FGC6C%2F%2BlL8IhkeKQ7JRJ3OzEUbXo5u3CHoR5Jj30O8w7%2BiqH1AZwU1hf5ps9Ksjr2AxDwZEstGsh99hXHa9tqzKYZcKUpI5zstAQHP9VvGthdZnyeDQTXALTaTO94XUpHcrlAVrgec8csYjgcv7V8XqGz%2B9WYYT6YK29woIYP3nz%2F7C5T76feS1GzPT0iI6w%2B68zPzefO%2FYpy20FaRciHOWlu2ftSDQwp9LMExsokPS7kgkS6nCnjEoUVsHghETivmwTTx%2BceQYm8ATX2EYKafOoqhe7Z3gUYnGIAl01RI6gTXbXsIx%2FHyHdsHsQuJCGUGQoOFXBcBEoB0CJute814KSvMKAcz92sC6doOOuhvGKzVwbAt6MGjvV4pfGkm%2BZP2Ti3xh6fbh8xm6zdjvmCbfeNrk2nXhD9LMls6vkAJOOracLimeqX1eCl3PTgpvr4NXGVR56fz7VuQGraTQhQH8Pqa%2BJjEk1mE6gFytH4XYw1UV1arqy5g2gtyaKLNdeKd%2BxajARM2vjAUdmybhvyoTLnagU1wTfHo5ov%2FrNTit%2F3QsWZeJvA3oSNJX1lk4%2B%2FlWwIv3wBg67CQcncrhGILQOOxc4ki0R3SwgjvIuv7ZG5I7vM0zxxuwG0w86KjWsWM9UPTPWD8Of7FP4H2zjIjcaZQAAirJ9p0hcUBvMabnVu1QdEjM8VmRjGw6qTuPqAP7bVMCD%2FKTikygpK3dN8Am7b9nQWvfe7LCnGyim%2FIicOAn8seRC5ojxw3ebJ5YzFb8C49xGk9U5itGswsHkQue%2Fw2O%2FK4j1YOZ3xJdUN4P3KtQfoVMuL58tGf3HDTkFv1khsB9g%2BbXb1jcmU3LqoYzyDvhtdOSrspOHBqeSu3OMboF3BL4329WqCnT59sc%2BYNGHktNFnN%2BFg3YswPY%2BSSMeO6%2BBmgAtLwetBTUNLWtYHn5Uyzah2GDweyl5hGypzMBZGIfWFQz6KE0tYmTJDEh7zUzzwoVSo4CexKsuDf5L1pz5769EeSAruChsJpADKpopCQr%2F2H51eUFdfa3GkZsVV9m4Nbfpobpnb%2FkNEyGWdPz4ACbd7X9Ua%2F1CGq1SgbZsw1%2B0QldEdaYU%2BnNbfNSu72tSLhkuQwohkV2%2BefFTaelD%2FZs3LgQiI9%2BKEdDjoAafrPg66%2B6Ns9lqyeB3ZXi2zzLI5Usbw7ZwAnqvn%2FNn0DjsRhW1XrhNB6iXiOUQuUMCzZgE08I6ozUandvUWTlIuTlshSi%2FZAcVGsuUaM1D28XnqsA2Vo1zJRuo2oTjudXRSC8Eek0Vf5mFQj9zoYy0CKDQ82REwyBjSpka0awoDO718ubWnj3KjCpO7K4GHjm%2F8toIfyre7XpMWZGJT19hhFeg%2FvKu%2F5HmDhNzsLeRmXbBEPZuK1946Kq3b9HCFs7tNw7tjDq1vAmyeo4B2yv8DEVtu9XX4df6kHO%2FVl3OXpqVCerzSk54bmlKuIbUjo7Za8ThJ74UMXiI5uAOx%2FtiwKu6A2%2B7oJZhju74KNgsPBIhCL3H2g%2Fz9td6huESbkg%2BKY0UJvEdrU95N71J%2BTKyI7OshaAvmjJ2QouIqMULRPp441i40EBdDjlGf9PmwCc0yk%2F0l4iCSgkS3saCei91pYSa1RIzbSbDM4KBA1kdX%2FssTJ%2BzzuuTJED0nreAvBPTPeh%2FxlmKV60R%2B1X%2FJSm0XbuWt013a3CcQxjmArG1aXWBS4fA8MOt%2FpqLe3iDydjMJPMC3vH67hQzoGCiAi4iN3MKnxktYItsdTZEyqdhFdHHAuiwXU0wIlGbJ0IB%2FGC6C%2F%2BlL8IhkeKQ7JRKDkiI4tLFgS4OjhBLTFWcx8w7%2BiqH1AZwU1hf5ps9Ksr6v%2FFlyKeKvYLtdUwTZoK%2Fq6fLM1HVeO0EH%2BaOYOZLh7jMG86m6XOLDBrNszNNwylA%2FvBPe02O6gaNgJ0SJd2FK2olLxScZ2Dl8Q5rlzYKxZadIHbcg%2BTeX%2F%2BZXVaH3xpewOaUYGu04S1thKPoK1JHRpAJj2lq%2BUV%2FiWRGfJ67grz8zjKR5xskyii%2FqRIYeGMeBY1M2HOtWDHDV%2BbE51g5RGNN20ojz2oxqq6hJwdY%2B2868Z2e3XnGHghH7ndum9YuoXB%2Fow%2BAVq6nS%2BQfwnULMyBLsWvSP4bWkduvka5ZdthNgCsR1X1pqYfwY4AkVWhMDOmFfd5HVn%2FPaXmM6xuwwtD0L4fGfYa8%2B%2B9luBT%2F1NK8amLQdWhgYnqlf8SGEFJ0ptglTxp%2FXO2ejSYL52ThyZJe49dItQe0WQ%2FxTmAUHatX7I8JOro%2BiCWZpL%2FRfBpmpjzrGLXZ64aX%2FnPqm2Oj5sr94Y1nSayI9oZcD7G6ewhy7XuVHMpYJEklTPgoHFkCfGWdFlbFaYIG1yEXlPnNStedWJUxDkZbkfjpwCfns8H6S80He6r41n1RyjCgFuh1iJybr7dhrwW9THyLTzs4z2yFaYG8SCS1SJJriXwa8QdO1E5wwjKkv0NLEzIGl8x0RiHFny8du8fXtSRcZIEP5He1F%2F2HaPD%2BjrRocPWuBgvgDPHYST0AEMKJ7JLsog27UTTs75EVMF1Qm9EirELU69u%2FprcarpozQbgV8iLKhveEAwkN2doKgUxtLH3Pu%2BAW%2FjJN48ZahFBzS%2BRyIryQV9LAJEW%2BR8H8wuVDh4ebRw2fS2cue47xh3WlqrGpjpwH3Oxyw%2F12gOOHqYOYrSV33NbjlpYTGp521rs%2FOtWuiq6YCIPEJuPhnecHDrld9aIiBl5jeQ3gkEmUgVHpymCwfW0yuc8QWfqikCS3BhFr%2Ba4GSXSV2olFKExFoPaMWsj9Pe8TvTnXyL7iHrFNUV%2BudGpZYs%2BpU3dovO4IBdBoU8lx%2FtSGHHp9wnt%2Bk8b96DFA%2FvBPe02O6gaNgJ0SJd2FK2olLxScZ2Dl8Q5rlzYKxZadIHbcg%2BTeX%2F%2BZXVaH3xqErsvruE6pP51lJI2G0NGATM9KFZhmsVrO6SI79zqalcob12FiWkpDFn1ApEttyuxeNDB4jqH%2FMfIxoYqSTQVxRGNN20ojz2oxqq6hJwdY%2B2868Z2e3XnGHghH7ndum9YuoXB%2Fow%2BAVq6nS%2BQfwnUImrMEpNPXjPYeQKurxzley956rgta8c1QeAjolkTD1ogdHlv233gp8a%2BiEb4ZJ7O32w0TBXggUdRkmja%2BRATgAV%2B%2FB8p4TlhVL0z0sjX93mESdpqAr2L4s7qwFoxhH7sgFAsn%2F7Tb8ChaCbHNoRXptRen9VVpuKDJkosB7YWb2nvnBIKRyno7tYgaV%2B6y9yJrfVh7CO8zJUMitYlgd2OttZcin8GqtCUfV89E6EFYS68%2FVirwdIJfNs4%2B8Kc%2BPf3E1p5kCMaELJiJW9aUibdIgJEeMdbvnMYP6hPWE4XiMmS8BW8AP76JgEGKMkgFp%2BYsn1K89hVQsxPGVGgS2j262jeI%2Fk4v6Si546h3XmAIF8eenm4Rs%2B%2Fpo97fyUvrwOsD4voGMpjibEGLNuwcXxtbuTfLQgtIt1NXvrNVZzvgzbLzhNqQiGNCxUWq24iVphYPQ3cl266doGsjE8rGd2TVBTF%2F9njaHOFPgivr0m42JBCisl%2BBezLjXkhxAU4aOShVyGeT%2FYKaLoWfkG%2FASynUfE5JtexWQUNL0WeF8JqhdE4E1Do6Z8ltVRcXCfG7%2BxcVTyUH2KWUefZOXssHkekjpUZYNMUn%2BrlyduSZciYcvotkU3PzjFeWC%2BuWQhR01EOlumI4%2FJTxze8agCpwQcrOjrE6zSZeradU6jes52IcBnyFHF7HMOhb6WPS5R6D99uo5znTPtnSCYch%2B4f1Q8U2vI2gPrNJwFetAjNfJLo4Fr%2BQfwIN93FyhrUoSB%2FNNUekVbtgvuGb6cjq0c9SvdiputmVs6dKaAE3goVQma79oMxC0k1KbbcCPWnx%2BfLInrfN4yWHZ%2FeFzlB4K80ZipsXRUw7InVZrxbejr7XNSxmhUSC4kjYpt6zH%2Be%2Bu%2Fgqf25BscZBPtF8k2fYp3qbW5Ekbv45VATBy1WMAM1j9wzJi5AGCss9LM9Q8OLNAXPgUQYELYDNuDIJkjPPNE5P3STu2m%2FCjQEeMsw2MwJ%2FXpuQ5%2FZ40yHriQluudYiUBT3MxTwkHr1wsXPEqx1B5tFYT5AtwtGQge2ySjBfSQDzVWBUEfdmoU03zXZb%2B5MD6nKrP4feDNRjEnFw7tTKPMQuHEYYq%2BeW%2BIK%2Fzex4HfzVV%2BSMAxzCwVJHRJeNREj6ZmT2FPe9BkJB2SmD1CeMHE9R2PDQybShOxp0b1%2FutKduiOaGCP70Szvuv8fhMKGHwanUL08a%2F0KV2odgJ8RZzUokEsaOsQrW%2B%2BKGgM0IHqhqaol%2B3vqUzwyK1y1gO8dEhwAgnhSeogEfoHtO7HZyxeWrm8rk9Puld20aSu19%2BcrYWDuV8AQwCOQnqYY7Xhv9DR6JpNeJhIeMnh%2BuPdmMZ%2FRe6mcU4xHImy7meRahrGoRQ7UE3Tzh07KFpdEw3P5S%2B30QdEcd4bqPnj55oT1Q0ir66sDFWQihA4xaYVEImYpeIjxv52KllSer2G7gIvB9ldHiBz8uBJNRFt0UDfR1IntizgNfnkG4hYrHZFsrHvh%2BipvkQQH3Oxyw%2F12gOOHqYOYrSV0VaCEwGEUWsoPYkS%2BQUTcW92B7SqLVmh49T%2BIXClybVfD8fZS4%2BIQ2aBlfIbgcZFcmRpGhNKMBZrsdv4h7PS5m4eycP7%2BqAzfj%2BCDww%2BFdboiBcDFbUd2LrlstOtrdAGQJL%2FnOYoqdzhok81V2T%2F34uOz5Zvb53aMFE606g9TVUmzikW%2Fzg4HXMZ%2FQFihlY3lrzDLVDNEtKVI9SeSPPBx1pPdJU1PVnUikItKPZLCYvgU%2BKRagyG9bcvun9yeF2gScvzNu6dJBK8CnztZWdCjN5lrru2qXoBFUPAEh0LJUe0Z2xNgkZfn5DqimHxvUcKCzCm9MXN1OPIT3Kqh0NKWdSH8TxcQdz896uZGuiKVgfQhVdIVcOJjrxo%2FTOymrU89dxENgZoh3QIP5ud3E78jaN%2FQ%2BBxdX%2B8ITWzK%2BTSih5QvL8IlS%2BhOkm7BCVA04eHVTtRrYB3BIfE6NUqHuIcjy1bkmGZV5d1U6D5zci74JQrz0mfCMJoOvNxx%2BFUkiSGCDNY9ciEddZHNiOjYF1n5KarPyxxdHJSAsPsqopjv9f%2BngD7MR2bb86Yk2%2B6TmOdroHeHhWRnSLQk5OLK0g6qtqEfDOLD77pMUTeY9RaVB%2F5qOxzYAsC8zsM6fnoz4K4n5He1F%2F2HaPD%2BjrRocPWuBgvgDPHYST0AEMKJ7JLsogzqgDT7cnUxu%2FOILcU3UgWM69u%2FprcarpozQbgV8iLKhveEAwkN2doKgUxtLH3Pu%2BAW%2FjJN48ZahFBzS%2BRyIrySkzfOpANID7Rk5OX2Y82Ud0bGlMA7TjrAXli%2FxgfrgWgH3Oxyw%2F12gOOHqYOYrSV3cDy0A7BtLHdz9a4GRPwwhq6YCIPEJuPhnecHDrld9aKTXYdjV79PLnwJaOMFwREMfW0yuc8QWfqikCS3BhFr%2Bvtth6gNz2nTqUSgvrmNbfD9Pe8TvTnXyL7iHrFNUV%2BudGpZYs%2BpU3dovO4IBdBoUGHf6Z9DzcCMcsU38%2BRKXkFbgG9p3heL7cXGOmsgh7ZpMB%2F0RUMikyyMRx5M904LbmKLfvXSsCPkwstiwjeuqxFA%2FvBPe02O6gaNgJ0SJd2FK2olLxScZ2Dl8Q5rlzYKxZadIHbcg%2BTeX%2F%2BZXVaH3xrecyS97%2Flff9CvyeO2D0FvRzbbCQab5iFVcKQpy32J%2Bps782MhVFh%2B%2B56d4RJdyDlSI7P9yITvP8AE%2BOh0j68JRGNN20ojz2oxqq6hJwdY%2B2868Z2e3XnGHghH7ndum9YuoXB%2Fow%2BAVq6nS%2BQfwnULMyBLsWvSP4bWkduvka5ZdIHYC563CNP336Ce17GZtxeEv%2FcVPCrqNbZVmvOKg%2BsRHN8ccapSyI8jTR2HWyuLVlaFynCy34WU5%2BpPEachJ96tUYgUrtXGqkSVuo4n2RhLO5uZd%2Byth1zJVf4%2Boe9NIf8AaxhgQQAnlovipnheagFlQir%2B8il9mlsgQQzRmMifRzxukZaRHWlfYupMRG8QUEz5gp9hzDigBCwFWW6F8p8OwyEYsz04QGQ7M2z8BpYxmUxEYmcT8dDxKAEqsPcXH33D4suKwItxxm2mSJ6ISM47%2BigXsEv1ZjMlWO01kl0i2axZb6tGmLT38GAuJyVNbvkEeBZc37S8ku%2B6ejovBEuOIzyoyAGUj2UAPS72B6nL6Ix%2BASuQsap8kHEVSsDjsF7wOSpQucHdLWMDfkDTr4Jeh1sGmE3dzYMP3TJ5t5uWxjzQauoeSQdgd%2BIf%2F2K0pDqXN097W06nNMsWgPajuBJVbg6aiWOI6rTrKB5MctwX%2B%2BncmxqKVo9IvVZTcu1zyNNWNWAgLywM49Lf7UllzyUAYTNdBf4NUOvDChleT9aI1s%2BoswJMqIwzNhhSePJZb4lW9qFdg354FdjTvp0ORgrjDi273GnCkKA%2Fqj1ilSlwd3VWBX0fqFczwVvRn3SJ4NJZuj4msJpBcmYtdViRm%2BsIWCHzpqgtRR6GetIXct7hpOUabK2%2B2UJCViVNLMAXcb22FOBXdtMoi6J%2BDEeq%2FGaKYXd26eeWcHykuQr1TK2%2B2ztcU%2BVAfc0%2FVmFp2SDHjlOjp7teegMQV0QPM8NBHDbRfL78X4e7hHKvpE4We%2Fgz9HihC%2FF52If7DvhQV4Sy0qkAWnS4g7vKjb0sqKXfWoCCEKYYbOm84GckclPN9auxb6YD9GLZwzbOROcSx%2FDQGRNJbQXlZyc8n12lzn7dbHqdh2MCBYlCbJNZmzMpZX6L%2Btf%2BY4fI%2F4iFxE7l0oRguyXaDtXQf2y%2F49RTmCn%2BQ04h3GXwcDtnCzJ%2B4QfP7OSIxFkMBMGNnq9KvT%2BgqFUmUVigEGN4AeUyYMkdMnyaIvsUE5CEMreHTwU4PD1w7LC8SUWNq0s4RTys%2FU21hoqq55IcIiFVrHU%2F%2Bq8%2FlTM4ohUkOZ87eKIEDweS15R2Uhr%2BQ3fc7k1F3V00jyp%2FHAj1Uhlb9a8tS1fCYjfnc04PFcvvNl3AdlKqBBIDo1ZTMIO6mxd%2FReqijWwAMwyavPpgAlXs9hclnL8k0FxEv0g%2FvD5sJTR8YdLhrrxYY2unx2fenXXIqbGXNXLejST6g9U2JR39prIrsY8dy41i%2FcGu1Jtlp%2BOXwFBYT3nLCH2bt8NeijJKjcgQXgcfvrM%2Fbi9v8UkoewxIEqwKDyiatxYpN0GEK23cNiaWpmzil7uEottCgQ%2Bj7Yi8vwNw86keFBjJ1%2F8VeC6dVVWShBapjzB8wD0AYTNdBf4NUOvDChleT9aJputyzFlPYO3hCh0cT7rQqbcMZu16OMnAqEePeDFZXVbxAACGQIlaQuJWJJ9TuawU4fOuzd%2BHauNbG0ZGxqfDLiZlmOR9TWQJyv5xv7zCFpXePpQQN5LLXzQpP3RhRHGeWQLsYQQ7tNv3vK2GxqhQ3QHtlXsQL6OO%2FueoB7LfJ9OjbYHJzLHND1SvxjycYhFYbUgqHUumlbTbNe%2B5NcqnDF%2BZ4Fqdq1DOyjgHzrrK2uhHZdYFSKewdAfGWlXQxoDQBK14cz1fmKqHmdri0WaPnz65qywEp5wG6eK4e0I0NZkO94pzq3Gnw3b0iIEi12rAwuRluvol2Iut2d%2F5jzSPxe1mwn0S%2BNtFqw8t531Mvg0b2Fz%2Br7YZ%2F3SJVzxT3ZLTO6xDyJ6MeNVAkBCtFPvT%2FK20a%2FCkEkoC0FJ6jFxLTYVa7MrQygeezHlu4Ea5SieH6n0NyVK%2F5p53%2FP71q0SExvmOCVUsa5f148AstyQYopzS6sSSh9z0RnHiqsB%2FgYT82CkisDIIp37pNpj54g0hi04WRVkb9rYwLwspqjGCwZciuHyrfFPW6atpcv%2FSDiEQGNQC5RUmaqG156N9%2BM97I66dvlHSr8s4S1YNDJVoXZmBIQNtFgeAMISU3io9FDQ09wf334hQx%2FNTxwNckjDFGwaIFYmdonHafYxoBqu9P5sjFWGGoebgmNufswasRqbnzIpBk4XU85QU2w%2F7XAdy6IsKEPEe5R2jrqWN8NcJHuVLj49tqeT%2FLqE%2F4hPNZdKSiVqimgAPequ7z9eH1FsECoAMx7bk1Z2rvZeK7vI4fYLhXd7otUbKW4ErY5xw%2FkVILFZfSRbNWB51OyDyrAv7ujy%2BOWBNLADEXBXZfi6U8YHOn30XC%2Bop847EQaZuWIaARu6EuB3An1eAyfC1DlNGNAdj78zpCjQ0g6uUhH6pmXxnaQL1%2BNZr9VOvCCWPjcW6LA%2FqyervgWDdanJmVTWxOSCE27i4omWfwtsHeH%2FmpSDXO9Vi951BUqPT3bnVFMjZcS805RsTCzUjM%2B2PlzvdtenIf0BgY4JHpcnw%2Fkr%2BE%2F24p3ySGAOnIfrBx%2FketZgG0tXUqpuZpxzG7k4M704ocSoiKr%2BNngC5cE7aHZytEsZuEA0BNPlykxIhngQD69ac9CM5qGvmWEv9PV95J%2B7KJxOEcEdaT%2FjlTgiVOizYU77kKTvgOxUKoXApJ1IaBggLzy4hBoinVqTxQSTaDjvzrGZ%2B6Zt1dAl8owjOZ6YRPCfJ78V5Y%2FqgAxQe1eEohmPViLsci4wQJSanPHtoEiIZn5kV%2Blel3tkuGdlTE3aMxPSUOpb1iWOTuD2c8XJwKN1ZZH9oN1Vdjx6d8x8Dswas%2FDtiH17ZDhFyF2NByaBK8AhAy2q0SDgA6fCPDWCvIpjOOJjvni2Plovad4kSRYVNC%2BVf6rmrk6fdxfyxdSjPP8%2FtuMrU%2FvCLddzidrnDP0SOfN2qU%2FBWPbu3UmKo3YXYQCi3Bv7OjpWtaXvsXLxKeJgBSNQmBwWZJODPAVUL1v6sDU%2FFfMQd8Thj7dES7lhIs2QBl75QzPQUENR3xUgNgCAQwCOQnqYY7Xhv9DR6JpNdYl9evxD8OjPkseUVuR5iT4xHImy7meRahrGoRQ7UE3ZdHLBdIEGQ7KjXxaWgEI%2FFLlm1J0Fgf0DUBfdB%2FAfIDveMGIlSh3YtGOxD96HCMwxXPWu%2BmqF2tdlqRkq7bwf3yBmo6VMbf44eYVl8Pf%2Fv5g8j6q6oQzU3HxpjlpS%2F2MTPFz2kdQ12m7cLZKHyJuQKQt5lUuN1VjImvhSjYTZv1AIteyH4UF3p6zxtFOqC9cHVVj60kuQRdq7YUFYn2RD55QhJVZ8SKfwnqlQFAXcrsqmu6jYR81bUEtsbwctBKiADO5Sf%2FqiTcgwD6%2FN4paOpvbYU4Fd20yiLon4MR6r8Zophd3bp55ZwfKS5CvVMrb7bO1xT5UB9zT9WYWnZIMeNwnXOd6A44Lrdoptemu50Wyq%2FxMt64ad61WrpcbG9c1YRrGNW6eHoz9UDiQBW9%2FTXT0nCBGmnFSpLhOG6OX%2BVDP1AnU%2FbyGUf5CV3V8tURwQv%2FaXtV2b%2FOpljS%2BTELJQhrIh7UC33rlnqwwSNsuK3WF1aXv228FXH80BlrvBu6lQEKs8RPsknBS8VL9I3GqtrY6IzmzB9PkVnLHpNdsNy1dAq4EdtWj9fwHx5%2FHmvNzOrFz9GaxmFxvUG5dBR6U759LjFT8hBqwS9hurR%2FJQR9LIs%2F3FbuyU1QkEcgFKWjHS2csoZtQx%2FbO%2BXEWCd5Ns1w0rwjM2qM18RXs%2FeBxZWwHo5gLiUzu9WPmm21PbX%2B3J%2F1rCg9JO87Ju8opEerbokT%2BPeurpVJvgflhIX18ExH782SJgiSXcqOEDgHVaiP93RPRj9PdLHGu%2FDJ5S1U9lVY7EXyLFz0wAdyKAKR5TKYt7WEtJVTLc%2BCzoxHMrVd0kZ0mmilb6foGcnHHgiOidHH2skciP3qtIPtrkR5z%2FyXlzHrwYBG8sVkWCgjhrlgIifuL21hFihD6KLLoWey%2FU3Ryif9FIzGoyJGxB5EzMV3gKlArnHNdkLsUcS4ftmGWXwnqqOnEy5%2BDVm9VuU42RLXyR70MenA8BQyRm50sllu1Z7DXj5aBNcZfewcj2lLXnYag%2B%2F4Ep3FcaC%2FJEDCyijFKx2eHjG2Mu9hLpCbuz0%2Fbo6h8LQNWPowbxCO%2FkK42VutY8QClJ1Onmek0J6MSbhGdsTYJGX5%2BQ6oph8b1HCgswpvTFzdTjyE9yqodDSlnQ8Lyr1tHp2uok%2BnTkhwTMLWVwq0wbb7jryOV7E3eMc45yHufGMW1hMoJXOaPQ1SlS2XYt67YqgSVKPhXLERRqXZ3Hudmy07cBYh8LVvDV2d3edSvqO7KMjHpIAHm535H10423cz5kX23ODcVNtjjJaafH2q7GzdELyEyJ9NBnvVYdH%2BidPDESZBCAmLsebHpxIrPa0kR9bOqh8s8Bwi1dK7Ox1f%2BkCwwh4CtUL7YLUlvGKcLxCFQ%2B%2FA8wt6gKWXZNHiyxoZb5V6NwGtuFIXdFLjb6I%2FT42me6rKXk1z%2BrRrLpifFrnQRxitYV8I%2BoAozuFU97Djk5LwgZnmXPrdOME6c4Wh%2BozAyLLxwn3FbGPVQu6kGa5juzsPEZfDfczJrQZZJh00V7oDKECskg6XUTXmZqaJYI%2BR4HCoMHQTQFAGSUoPAN6sM2TA41%2Bkp1igE38f9WaGYkKcA9xMiYKrmSwqGyOdcbGrFaN0FhKskNeSDnibmc6g8vJ97tLOFyr3tveURDRc0WKaSWC4j8IwrFUD1VvFNpCdQbrbBtEYSO3hci0mFtyt8df9ASyqr6utyJw4XC7PNCvgMYzE2bTUF%2FjiVb2oV2DfngV2NO%2BnQ5GCuq86rYO71ndUKO6kRkDtpjBCM1Rb90%2FgisWk6EH0ZBKuacufp3%2F1UXItdnMweXHiLIIVoldAJMXfmRPAO0uizAtRe1B%2FXtI3uKR%2BYNo1Cl1DLHPv9e6Iq5%2Bh5QRVFJzmPFwPyG8mZMWsiMAREPmr%2B9N631FAZEIdYcH2SWo%2Fp8t7c8jyCdJCTDWRcd3SBYq3KIElvaLZ2ggCbWpxJbunthHCpuJrKahxhph2N6Y9RWltwc3WA%2Bm1vYWPouzSo8JnAoO6dyj9hG1WmfA0HtTRMhT1hFjrVd3%2BnUL2UMpFZ2VcQ0dP62uYG6Hi3nBUaEt998JrXrm7rZq6H3kq4%2FRvshLq3kHpNrKzCtWO8uvpAOnO3RRO3Tt3wxkFLuECIhfVOVh%2BcG%2FPc%2F5peSwpZcABMI0mCQruAiKbC4q3Jq8UM6xxybXujJ92fs0iXzLm%2BsUh";


    private void test() {
        String s = EncodeUtils.urlDecode(JsonUtils.getString(editText.getText().toString(), "result"));
        System.out.println(s);
        String result = AESUtils.m21238a(EncodeUtils.urlDecode(h));
        System.out.println(result);
    }

    public void button(View view) {
        test();
        ImageView bigImage = findViewById(R.id.mBigImage);
        GlideApp.with(this)
                .load(Uri.parse("https://gkoss.banzhengkuai.com/comic_chapter/18/f3/1218f333c41cd48bf86d0b834c94f306cfca834167.ceb"))
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(bigImage);
//        final String url = getCompleteUrl(editText.getText().toString());
//        // 解析到url
//
//        if (subscribe != null && !subscribe.isDisposed()) {
//            subscribe.dispose();
//        }
//        subscribe = Flowable.create(new FlowableOnSubscribe<String>() {
//            @Override
//            public void subscribe(FlowableEmitter<String> emitter) throws Exception {
//                Document doc = Jsoup.connect(url)
//                        .userAgent("Mozilla/5.0.html (iPhone; U; CPU iPhone OS 4_3_3 like Mac " +
//                                "OS X; en-us) AppleWebKit/533.17.9 (KHTML, like Gecko) " +
//                                "Version/5.0.html.2 Mobile/8J2 Safari/6533.18.5 ").get();
//                Elements video = doc.select("video[src]");
//                for (Element el : video) {
//                    String videoUrl = el.attr("src");
//                    videoUrl = videoUrl.replace("playwm", "play");
//                    // 获取重定向的URL
//                    videoUrl = getRealUrl(videoUrl);
//                    emitter.onNext(videoUrl);
//                }
//                emitter.onComplete();
//            }
//        }, BackpressureStrategy.BUFFER)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Consumer<String>() {
//                    @Override
//                    public void accept(String videoUrl) {
//                        tvResult.setText(videoUrl);
//                        Intent intent = new Intent(MainActivity.this, SimplePlayer.class);
//                        intent.putExtra("video_url", videoUrl);
//                        startActivity(intent);
//                    }
//                }, new Consumer<Throwable>() {
//                    @Override
//                    public void accept(Throwable throwable) {
//                        throwable.printStackTrace();
//                    }
//                });
    }

    /**
     * 获取完整的域名
     *
     * @param text 获取浏览器分享出来的text文本
     */
    public static String getCompleteUrl(String text) {
        Pattern p = Pattern.compile("((http|ftp|https)://)(([a-zA-Z0-9._-]+\\.[a-zA-Z]{2,6})|([0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}))(:[0-9]{1,4})*(/[a-zA-Z0-9&%_./-~-]*)?", Pattern.CASE_INSENSITIVE);
        Matcher matcher = p.matcher(text);
        matcher.find();
        return matcher.group();
    }

    private String getRealUrl(String urlStr) {
        String realUrl = urlStr;
        try {
            URL url = new URL(urlStr);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("user-agent", "Mozilla/5.0.html (iPhone; U; CPU iPhone OS 4_3_3 like Mac " +
                    "OS X; en-us) AppleWebKit/533.17.9 (KHTML, like Gecko) " +
                    "Version/5.0.html.2 Mobile/8J2 Safari/6533.18.5 ");
            conn.setInstanceFollowRedirects(false);
            int code = conn.getResponseCode();
            String redirectUrl = "";
            if (302 == code) {
                redirectUrl = conn.getHeaderField("Location");
            }
            if (redirectUrl != null && !redirectUrl.equals("")) {
                realUrl = redirectUrl;
            }
            conn.disconnect();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return realUrl;
    }

    public void onResultClick(View view) {
        myClipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        String text;
        text = tvResult.getText().toString();

        myClip = ClipData.newPlainText("text", text);
        myClipboard.setPrimaryClip(myClip);

        Toast.makeText(this, text + " 已复制", Toast.LENGTH_SHORT).show();
    }
}
