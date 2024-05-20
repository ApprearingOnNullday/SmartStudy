-- 建库
create database if not exists SmartStudy;
use SmartStudy;

-- 建表
CREATE TABLE `tb_user`
(
    `id`       int unsigned NOT NULL AUTO_INCREMENT COMMENT '自增主键ID',
    `username` varchar(32) 	NOT NULL DEFAULT '' COMMENT '用户名',
    `password` varchar(32) 	NOT NULL DEFAULT '' COMMENT '密码',
    `role`     tinyint 		NOT NULL DEFAULT 0 COMMENT '角色',
    `deleted`  tinyint     	NOT NULL DEFAULT 0 comment '软删标识，0：未删除，1：已删除',
    `ctime`    DATETIME    	NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `mtime`    DATETIME    	NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    PRIMARY KEY (`id`),
    INDEX      `ix_mtime` (`mtime`)
) ENGINE = InnoDB COMMENT ='用户表';

CREATE TABLE `tb_course`
(
    `id`       		int unsigned 	NOT NULL AUTO_INCREMENT COMMENT '自增主键ID',
    `title` 		varchar(32) 	NOT NULL DEFAULT '' COMMENT '课程名称',
    `teacher_id` 	int 			NOT NULL DEFAULT 0 COMMENT '教师用户id',
    `description`	varchar(32) 	NOT NULL DEFAULT '' COMMENT '课程描述',
    `deleted`  		tinyint     	NOT NULL DEFAULT 0 comment '软删标识，0：未删除，1：已删除',
    `ctime`    		DATETIME    	NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `mtime`    		DATETIME    	NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    PRIMARY KEY (`id`),
    INDEX      `ix_mtime` (`mtime`)
) ENGINE = InnoDB COMMENT ='课程表';

CREATE TABLE `tb_homework`
(
    `id`       		int unsigned 	NOT NULL AUTO_INCREMENT COMMENT '自增主键ID',
    `title` 		varchar(32) 	NOT NULL DEFAULT '' COMMENT '作业标题',
    `course_id` 	int 			NOT NULL DEFAULT 0 COMMENT '课程id',
    `description`	varchar(1024)   NOT NULL DEFAULT '' COMMENT '作业详情',
    `start`   		DATETIME     	NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '开始时间',
    `end`     		DATETIME     	NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '截止时间',
    `deleted`  		tinyint     	NOT NULL DEFAULT 0 comment '软删标识，0：未删除，1：已删除',
    `ctime`    		DATETIME    	NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `mtime`    		DATETIME    	NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    PRIMARY KEY (`id`),
    INDEX      `ix_mtime` (`mtime`)
) ENGINE = InnoDB COMMENT ='作业表';

CREATE TABLE `tb_homework_read`
(
    `id`       		int unsigned 	NOT NULL AUTO_INCREMENT COMMENT '自增主键ID',
    `student_id` 	int	 			NOT NULL DEFAULT 0 COMMENT '学生id',
    `homework_id`   int 			NOT NULL DEFAULT 0 COMMENT '作业id',
    `status`		tinyint 		NOT NULL DEFAULT 0 COMMENT '已读状态',
    `deleted`  		tinyint     	NOT NULL DEFAULT 0 comment '软删标识，0：未删除，1：已删除',
    `ctime`    		DATETIME    	NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `mtime`    		DATETIME    	NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    PRIMARY KEY (`id`),
    INDEX      `ix_mtime` (`mtime`)
) ENGINE = InnoDB COMMENT ='作业已读表';

CREATE TABLE `tb_submission`
(
    `id`       		int unsigned 	NOT NULL AUTO_INCREMENT COMMENT '自增主键ID',
    `student_id` 	int	 			NOT NULL DEFAULT 0 COMMENT '学生id',
    `homework_id`   int 			NOT NULL DEFAULT 0 COMMENT '作业id',
    `submit_time`   DATETIME     	NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '提交时间',
    `content`		varchar(1024)   NOT NULL DEFAULT '' COMMENT '提交内容',
    `status`		tinyint 		NOT NULL DEFAULT 0 COMMENT '批改状态',
    `deleted`  		tinyint     	NOT NULL DEFAULT 0 comment '软删标识，0：未删除，1：已删除',
    `ctime`    		DATETIME    	NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `mtime`    		DATETIME    	NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    PRIMARY KEY (`id`),
    INDEX      `ix_mtime` (`mtime`)
) ENGINE = InnoDB COMMENT ='作业提交表';

CREATE TABLE `tb_grade`
(
    `id`       				int unsigned 	NOT NULL AUTO_INCREMENT COMMENT '自增主键ID',
    `submission_id` 	    int	 			NOT NULL DEFAULT 0 COMMENT '提交id',
    `score` 				decimal(5, 2)   NOT NULL DEFAULT 0.00 COMMENT '分数',
    `comment` 				varchar(255) 	NOT NULL DEFAULT '' COMMENT '教师评语',
    `deleted`  				tinyint     	NOT NULL DEFAULT 0 comment '软删标识，0：未删除，1：已删除',
    `ctime`    				DATETIME    	NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `mtime`    				DATETIME    	NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    PRIMARY KEY (`id`),
    INDEX      `ix_mtime` (`mtime`)
) ENGINE = InnoDB COMMENT ='分数表';
