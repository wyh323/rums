package com.happy_hao.rums.po;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("classifications")
public class Classification {

    @TableId("classification_id")
    private Long classificationId; // 主键ID

    @TableField("classification_name")
    private String classificationName; // 分组名

    @TableField("create_at")
    private Date createAt; // 创建时间

    @TableField("update_at")
    private Date updateAt; // 更新时间

    @TableField("description")
    private String description;

    @TableField("extension")
    private String extension;
}
