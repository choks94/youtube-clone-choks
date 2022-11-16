package com.choks.youtubeclone.service;

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

    public void uploadVideo(MultipartFile multipartFile) {

        String videoUrl = s3Service.uploadFile(multipartFile);
        var video = new Video();
        video.setVideoUrl(videoUrl);

        videoRepository.save(video);
    }

    public VideoDTO editVideo(VideoDTO videoDTO) {

        Video savedVideo = videoRepository.findById(videoDTO.getId()).orElseThrow(() ->
                new IllegalArgumentException("Cannot find video by ID: " +videoDTO.getId()));

        savedVideo.setTitle(videoDTO.getTitle());
        savedVideo.setDescription(videoDTO.getDescription());
        savedVideo.setTags(videoDTO.getTags());
        savedVideo.setThumbnailUrl(videoDTO.getThumbnailUrl());
        savedVideo.setVideoStatus(videoDTO.getVideoStatus());

        videoRepository.save(savedVideo);

        return videoDTO;
    }
}
