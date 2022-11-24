package sample;

import com.google.common.cache.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public class Test {

    public static void main(String[] args) {

    }


}
   /* private LoadingCache<String, String> cache;
    private static Test gt = new Test();
    private Test() {
        cache = CacheBuilder.newBuilder().refreshAfterWrite(2, TimeUnit.SECONDS).build(new CacheLoader<String, String>() {
            @Override
            public String load(String s) throws Exception {
                return addCache(s);
            }
        });
    }

    public String addCache(String arg0){
        System.out.println("add Cache");
        return arg0.toUpperCase();

    }

    public String getEntry(String arg0) throws ExecutionException {
        System.out.println(cache.size());
        return cache.get(arg0);
    }

    public static Test getInstance(){
        return gt;
    }

    public LoadingCache<String, String> getCache(){
        return cache;
    }
    public static void main(String[] args) throws ExecutionException, InterruptedException {

     /*   Test gt = Test.getInstance();

        System.out.println(gt.getEntry("beka"));
        System.out.println(gt.getEntry("beka2"));
        Thread.sleep(2100);
        System.out.println(gt.getEntry("beka3"));
        System.out.println(gt.getEntry("beka4"));


        Thread.sleep(2100);
        System.out.println(gt.getCache().asMap().values());*/

      /*  CacheLoader<String, String> loader;
        loader = new CacheLoader<String, String>() {
            @Override
            public String load(final String key) {
                return key.toUpperCase();
            }
        };

        RemovalListener<String, String> listener;
        listener = new RemovalListener<String, String>() {
            @Override
            public void onRemoval(RemovalNotification<String, String> n){
                System.out.println(n.getKey() + " : " + n.getKey() + " : " + n.getCause());
                if (n.wasEvicted()) {
                    String cause = n.getCause().name();
                    System.out.println("TESt: " + cause);
                }
            }
        };

        LoadingCache<String, String> cache;
        cache = CacheBuilder.newBuilder()
                .maximumSize(2)
                .removalListener(listener)
                .build(loader);

        System.out.println(cache.getUnchecked("first"));
        System.out.println(cache.getUnchecked("second"));
        System.out.println(cache.getUnchecked("third"));
        System.out.println(cache.getUnchecked("last"));

        System.out.println("Size: " + cache.size());

        Thread.sleep(2100);

        System.out.println("Size: " + cache.size());
        System.out.println();
        System.out.println(cache.asMap().keySet());*/