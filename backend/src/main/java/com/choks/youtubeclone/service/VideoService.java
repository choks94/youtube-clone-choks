package com.choks.youtubeclone.service;

import com.choks.youtubeclone.dto.UploadVideoResponse;
import com.choks.youtubeclone.dto.VideoDTO;
import com.choks.youtubeclone.model.Video;
import com.choks.youtubeclone.repository.VideoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class VideoService {

    private final S3Service s3Service;
    private final VideoRepository videoRepository;

    public UploadVideoResponse uploadVideo(MultipartFile multipartFile) {

        String videoUrl = s3Service.uploadFile(multipartFile);
        var video = new Video();
        video.setVideoUrl(videoUrl);

        var savedVideo = videoRepository.save(video);
        return new UploadVideoResponse(savedVideo.getId(), savedVideo.getVideoUrl());
    }

    public VideoDTO editVideo(VideoDTO videoDTO) {

        var savedVideo = getVideoByID(videoDTO.getId());

        savedVideo.setTitle(videoDTO.getTitle());
        savedVideo.setDescription(videoDTO.getDescription());
        savedVideo.setTags(videoDTO.getTags());
        savedVideo.setThumbnailUrl(videoDTO.getThumbnailUrl());
        savedVideo.setVideoStatus(videoDTO.getVideoStatus());

        videoRepository.save(savedVideo);

        return videoDTO;
    }

    public String uploadThumbnail(MultipartFile file, String videoId) {

        var savedVideo = getVideoByID(videoId);
        String thumbnailUrl = s3Service.uploadFile(file);

        savedVideo.setThumbnailUrl(thumbnailUrl);

        videoRepository.save(savedVideo);

        return thumbnailUrl;
    }

    private Video getVideoByID(String videoID) {

        return videoRepository.findById(videoID).orElseThrow(() ->
                new IllegalArgumentException("Cannot find video by ID: " +videoID));

    }

    public VideoDTO getVideoDetails(String videoId) {
        Video savedVideo = getVideoByID(videoId);


        VideoDTO videoDto = new VideoDTO();
        videoDto.setVideoUrl(savedVideo.getVideoUrl());
        videoDto.setThumbnailUrl(savedVideo.getThumbnailUrl());
        videoDto.setId(savedVideo.getId());
        videoDto.setTitle(savedVideo.getTitle());
        videoDto.setDescription(savedVideo.getDescription());
        videoDto.setTags(savedVideo.getTags());
        videoDto.setVideoStatus(savedVideo.getVideoStatus());

        return videoDto;
    }
}
