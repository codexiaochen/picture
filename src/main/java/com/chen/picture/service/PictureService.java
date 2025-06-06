package com.chen.picture.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.chen.picture.model.dto.picture.PictureQueryRequest;
import com.chen.picture.model.dto.picture.PictureUploadRequest;
import com.chen.picture.model.entity.Picture;
import com.baomidou.mybatisplus.extension.service.IService;
import com.chen.picture.model.entity.User;
import com.chen.picture.model.vo.PictureVO;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

/**
* @author abc
* @description 针对表【picture(图片)】的数据库操作Service
* @createDate 2025-04-22 21:39:47
*/
public interface PictureService extends IService<Picture> {

    /**
     * 上传图片
     *
     * @param multipartFile
     * @param pictureUploadRequest
     * @param loginUser
     * @return
     */
    PictureVO uploadPicture(MultipartFile multipartFile,
                            PictureUploadRequest pictureUploadRequest,
                            User loginUser);

    void validPicture(Picture picture);

    QueryWrapper<Picture> getQueryWrapper(PictureQueryRequest pictureQueryRequest);

    PictureVO getPictureVO(Picture picture, HttpServletRequest request);

    Page<PictureVO> getPictureVOPage(Page<Picture> picturePage, HttpServletRequest request);

}
