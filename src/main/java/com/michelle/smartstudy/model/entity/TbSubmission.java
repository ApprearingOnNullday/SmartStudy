package com.michelle.smartstudy.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 作业表
 * </p>
 *
 * @author AppearingOnNullday
 * @since 2024-05-20
 */
@TableName("tb_homework")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TbHomework implements Serializable {

    /**
     * 自增主键ID（作业id）
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 作业标题
     */
    private String title;

    /**
     * 课程id
     */
    private Integer courseId;

    /**
     * 作业详情
     */
    private String description;

    /**
     * 开始时间
     */
    private LocalDateTime start;

    /**
     * 截止时间
     */
    private LocalDateTime end;

    /**
     * 软删标识，0：未删除，1：已删除
     */
    @TableLogic
    private Byte deleted;

    /**
     * 创建时间
     */
    private LocalDateTime ctime;

    /**
     * 修改时间
     */
    private LocalDateTime mtime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        TbHomework other = (TbHomework) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
                && (this.getTitle() == null ? other.getTitle() == null : this.getTitle().equals(other.getTitle()))
                && (this.getCourseId() == null ? other.getCourseId() == null : this.getCourseId().equals(other.getCourseId()))
                && (this.getDescription() == null ? other.getDescription() == null : this.getDescription().equals(other.getDescription()))
                && (this.getStart() == null ? other.getStart() == null : this.getStart().equals(other.getStart()))
                && (this.getEnd() == null ? other.getEnd() == null : this.getEnd().equals(other.getEnd()))
                && (this.getDeleted() == null ? other.getDeleted() == null : this.getDeleted().equals(other.getDeleted()))
                && (this.getCtime() == null ? other.getCtime() == null : this.getCtime().equals(other.getCtime()))
                && (this.getMtime() == null ? other.getMtime() == null : this.getMtime().equals(other.getMtime()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getTitle() == null) ? 0 : getTitle().hashCode());
        result = prime * result + ((getCourseId() == null) ? 0 : getCourseId().hashCode());
        result = prime * result + ((getDescription() == null) ? 0 : getDescription().hashCode());
        result = prime * result + ((getStart() == null) ? 0 : getStart().hashCode());
        result = prime * result + ((getEnd() == null) ? 0 : getEnd().hashCode());
        result = prime * result + ((getDeleted() == null) ? 0 : getDeleted().hashCode());
        result = prime * result + ((getCtime() == null) ? 0 : getCtime().hashCode());
        result = prime * result + ((getMtime() == null) ? 0 : getMtime().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", title=").append(title);
        sb.append(", courseId=").append(courseId);
        sb.append(", description=").append(description);
        sb.append(", start=").append(start);
        sb.append(", end=").append(end);
        sb.append(", deleted=").append(deleted);
        sb.append(", ctime=").append(ctime);
        sb.append(", mtime=").append(mtime);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}
