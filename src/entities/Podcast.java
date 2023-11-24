package entities;

import fileio.input.EpisodeInput;
import fileio.input.PodcastInput;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

@Getter
public class Podcast implements AudioTrack {
    private String name;
    private String owner;
    private ArrayList<AudioFile> episodes = new ArrayList<>();
    private ArrayList<Integer> elapsedTime = new ArrayList<>();

    public Podcast(final PodcastInput podcastInput) {
        name = podcastInput.getName();
        owner = podcastInput.getOwner();

        int playtime = 0;
        for (final EpisodeInput episodeInput : podcastInput.getEpisodes()) {
            episodes.add(new Episode(episodeInput));
            playtime += episodeInput.getDuration();
            elapsedTime.add(playtime);
        }
    }

    @Override
    public AudioFile loadAudioFile(final Integer watchTime) {
        if (watchTime == null) {
            return episodes.get(0);
        }

        for (int i = 0; i < elapsedTime.size(); i++) {
            if (watchTime < elapsedTime.get(i)) {
                return episodes.get(i);
            }
        }
        return episodes.get(0);
    }

    @Override
    public void updateAudioFile(final MusicPlayer musicPlayer, int timePassed) {
        ArrayList<AudioFile> playQueue = new ArrayList<>(episodes);

        switch (musicPlayer.repeatState()) {
            case 0:
                if (simulatePlayQueue(musicPlayer, playQueue, timePassed)
                        != musicPlayer.getRemainedTime()) {
                    musicPlayer.setRemainedTime(0);
                }
                break;

            case 1:
                playQueue.add(playQueue.indexOf(musicPlayer.getAudioFile()),
                            musicPlayer.getAudioFile());
                simulatePlayQueue(musicPlayer, playQueue, timePassed);
                musicPlayer.setRepeat(0);
                break;

            case 2:
                if (timePassed <= musicPlayer.getRemainedTime()) {
                    musicPlayer.setRemainedTime(musicPlayer.getRemainedTime() - timePassed);
                    return;
                }

                timePassed -= musicPlayer.getRemainedTime();
                int audioFileDuration = musicPlayer.getAudioFile().getDuration();
                musicPlayer.setRemainedTime(audioFileDuration - timePassed % audioFileDuration);
                break;

            default:
                break;
        }
    }

    @Override
    public boolean atFirstAudioFile(final MusicPlayer musicPlayer) {
        ArrayList<AudioFile> playQueue = new ArrayList<>(episodes);
        if (musicPlayer.isShuffle()) {
            Collections.shuffle(playQueue, new Random(musicPlayer.getSeed()));
        }
        return musicPlayer.getAudioFile().equals(playQueue.get(0));
    }

    @Override
    public ArrayList<AudioFile> loadAudioList() {
        return episodes;
    }
}
