package com.github.sankowskiwojciech.courseslessons;

import com.github.sankowskiwojciech.coursescorelib.CoursesCoreLibConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(CoursesCoreLibConfig.class)
public class Config {
}
