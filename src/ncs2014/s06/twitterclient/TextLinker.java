package ncs2014.s06.twitterclient;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ClickableSpan;
import android.util.SparseArray;
import android.view.View;

public class TextLinker {

    public interface OnLinkClickListener{
        public void onLinkClick(int textId);
    }

    private static class TextClickSpan extends ClickableSpan{

        private OnLinkClickListener mListener;
        private int mTextId;

        public TextClickSpan(int textId , OnLinkClickListener listener){
            mTextId = textId;
            mListener = listener;
        }

        @Override
        public void onClick(View widget) {
            if(mListener != null)mListener.onLinkClick(mTextId);
        }
    }

    public static SpannableString getLinkableText(String text , SparseArray<String> links , OnLinkClickListener listener){
        SpannableString spannable = new SpannableString(text);

        int size = links.size();
        for(int i = 0 ; i < size ; i++){
            int key = links.keyAt(i);
            String link = links.get(key);
            Pattern p = Pattern.compile(link);
            Matcher m = p.matcher(text);
            while(m.find()){
                TextClickSpan span = new TextClickSpan(key , listener);
                spannable.setSpan(span, m.start(), m.end(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }
        return spannable;
    }
}
