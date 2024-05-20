package com.michelle.smartstudy.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <p>
 * 分数表
 * </p>
 *
 * @author AppearingOnNullday
 * @since 2024-05-20
 */
@TableName("tb_grade")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TbGrade implements Serializable {

    /**
     * 自增主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 提交id
     */
    private Integer submissionId;

    /**
     * 分数
     */
    private BigDecimal score;

    /**
     * 教师评语
     */
    private String comment;

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
        TbGrade other = (TbGrade) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
                && (this.getSubmissionId() == null ? other.getSubmissionId() == null : this.getSubmissionId().equals(other.getSubmissionId()))
                && (this.getScore() == null ? other.getScore() == null : this.getScore().equals(other.getScore()))
                && (this.getComment() == null ? other.getComment() == null : this.getComment().equals(other.getComment()))
                && (this.getDeleted() == null ? other.getDeleted() == null : this.getDeleted().equals(other.getDeleted()))
                && (this.getCtime() == null ? other.getCtime() == null : this.getCtime().equals(other.getCtime()))
                && (this.getMtime() == null ? other.getMtime() == null : this.getMtime().equals(other.getMtime()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getSubmissionId() == null) ? 0 : getSubmissionId().hashCode());
        result = prime * result + ((getScore() == null) ? 0 : getScore().hashCode());
        result = prime * result + ((getComment() == null) ? 0 : getComment().hashCode());
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
        sb.append(", submissionId=").append(submissionId);
        sb.append(", score=").append(score);
        sb.append(", comment=").append(comment);
        sb.append(", deleted=").append(deleted);
        sb.append(", ctime=").append(ctime);
        sb.append(", mtime=").append(mtime);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}
