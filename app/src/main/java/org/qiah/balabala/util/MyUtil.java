package org.qiah.balabala.util;
//public class MyUtil {
//    public static byte[] convertFileToByteArray(File file) throws IOException {
//        FileInputStream fis = null;
//        byte[] byteArray = null;
//        try {
//            fis = new FileInputStream(file);
//            byteArray = new byte[(int) file.length()];
//            fis.read(byteArray);
//        } finally {
//            if (fis != null) {
//                fis.close();
//            }
//        }
//        return byteArray;
//    }
//    public static byte[] getResBytes(int code,String message,String data) throws UnsupportedEncodingException {
//        return  (new Res(code,message,data).toString() +"\n").getBytes("utf-8");
//    }
//    public static void load(ImageView view,String url,Context context){
//        load(view, url);
//        view.setOnClickListener(v->{
//            ArrayList<LocalMedia>  list = new ArrayList();
//            list.add(LocalMedia.generateHttpAsLocalMedia(url));
//            PictureSelector.create(context)
//                    .openPreview()
//                    .setImageEngine(GlideEngine.createGlideEngine())
//                    .startActivityPreview(0, false, list);
//        });
//    }
//    public static void load(ImageView view,String url,int r,Context context){
//        load(view, url,r);
//        view.setOnClickListener(v->{
//            ArrayList<LocalMedia>  list = new ArrayList();
//            list.add(LocalMedia.generateHttpAsLocalMedia(url));
//            PictureSelector.create(context)
//                    .openPreview()
//                    .setImageEngine(GlideEngine.createGlideEngine())
//                    .startActivityPreview(0, false, list);
//        });
//    }
//    public static void load(ImageView view,String url){
//        Glide.with(view)
//                .load(url)
//                .centerCrop()
//                .circleCrop()
//                .into(view);
//    }
//    public static void load(ImageView view,String url,int r){
//        Glide.with(view)
//                .load(url)
//                .apply(RequestOptions.bitmapTransform(new RoundedCorners(r)))
//                .into(view);
//    }
//}
