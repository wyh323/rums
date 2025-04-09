package com.happy_hao.rums.po;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("groups")
public class Group {
    @TableId("group_id")
    private Long groupId; // 主键ID

    @TableField("group_name")
    private String groupName; // 分组名

    @TableField("create_at")
    private LocalDateTime createAt; // 创建时间

    @TableField("update_at")
    private LocalDateTime updateAt; // 更新时间

    @TableField("description")
    private String description;

    @TableField("extension")
    private String extension;

    @TableField(exist = false)
    private List<Permission> permissions = new ArrayList<>();

}
