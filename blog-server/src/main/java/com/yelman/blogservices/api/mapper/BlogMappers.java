package com.yelman.blogservices.api.mapper;

import com.yelman.blogservices.api.dto.BlogDto;
import com.yelman.blogservices.model.Blogs;

public interface BlogMappers {
    BlogDto mapDto(Blogs blogs);
    Blogs mapEntity(BlogDto blogDto);
}
