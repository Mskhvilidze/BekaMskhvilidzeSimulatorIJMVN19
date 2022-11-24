package sample;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import javafx.scene.media.AudioClip;
import sample.presenter.Presenter;

import java.io.File;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * AudioCache
 */
@SuppressWarnings("NullableProblems")
public final class AudioCache {
    private static final LoadingCache<String, AudioClip> SOUNDS = CacheBuilder.newBuilder()
            .expireAfterAccess(10, TimeUnit.MINUTES)
            .maximumSize(100)
            .build(new CacheLoader<String, AudioClip>() {
                @Override
                public AudioClip load(String key){
                    return new AudioClip(key);
                }
            });

    /**
     * Empty private Constructor
     */
    private AudioCache() {
    }

    /**
     *
     * @param key The audio_url,
     * @return an AudioClip
     */
    public static AudioClip getAudio(String key) throws ExecutionException {
        String audioPath = Presenter.SOUND_PATH + key;
        return AudioCache.SOUNDS.get(new File(audioPath).toURI().toString());
    }


}
