package com.chen.picture.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chen.picture.model.entity.Picture;
import com.chen.picture.service.PictureService;
import com.chen.picture.mapper.PictureMapper;
import org.springframework.stereotype.Service;

/**
* @author abc
* @description 针对表【picture(图片)】的数据库操作Service实现
* @createDate 2025-04-22 21:39:47
*/
@Service
public class PictureServiceImpl extends ServiceImpl<PictureMapper, Picture>
    implements PictureService{

}




