package com.qingge.springboot.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import java.time.LocalDate;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 
 * </p>
 *
 * @author wyc
 * @since 2022-05-21
 */
@Getter
@Setter
@ApiModel(value = "Food对象", description = "")
public class Food implements Serializable {

    private static final long serialVersionUID = 1L;

      @ApiModelProperty("编号")
        @TableId(value = "id", type = IdType.AUTO)
      private Integer id;

      @ApiModelProperty("食品抽检编号")
      private String code;

      @ApiModelProperty("食品名称")
      private String productName;

      @ApiModelProperty("食品生产厂家")
      private String productCn;

      @ApiModelProperty("食品生产厂家地址")
      private String productCa;

//      ......

      @ApiModelProperty("生产地省份")
      private String productProvince;

      @ApiModelProperty("销售公司")
      private String saleCompany;

      @ApiModelProperty("食品销售地址")
      private String saleCa;

      @ApiModelProperty("销售地省份")
      private String scProvince;

      @ApiModelProperty("生产日期")
      private LocalDate productDate;

      @ApiModelProperty("食品类别")
      private String classify;

      @ApiModelProperty("抽检单位")
      private String sampleC;

      @ApiModelProperty("抽检日期")
      private LocalDate sampleDate;

      @ApiModelProperty("备注")
      private String comment;

      @ApiModelProperty("是否合格")
      private String qualified;


}
