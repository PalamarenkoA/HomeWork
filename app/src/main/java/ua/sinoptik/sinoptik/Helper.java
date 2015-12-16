package ua.sinoptik.sinoptik;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by Админ on 16.12.2015.
 */
public class Helper {
    static void showToast(String text, Context context) {
        //создаем и отображаем текстовое уведомление
        Toast toast = Toast.makeText(context,
                text,
                Toast.LENGTH_SHORT);
        toast.show();
    }

static int showIcon(String icon){
  switch (icon){
      case("01d"):
          return R.drawable.icon01d;
      case ("01n"):
          return R.drawable.icon01n;
      case ("02n"):
          return R.drawable.icon02n;
      case ("03n"):
          return R.drawable.icon03n;
      case ("04n"):
          return R.drawable.icon04n;
      case ("10n"):
          return R.drawable.icon10n;
      case ("10d"):
          return R.drawable.icon10d;
      case ("13d"):
          return R.drawable.icon13d;
  }
return 1;}
}
