package com.ruoyi.campus.asset.domain;

import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 校园资产
 */
public class CampusAsset extends BaseEntity
{
    private Long assetId;

    private String assetNo;

    private String assetName;

    private String assetType;

    private String location;

    private Integer totalQuantity;

    private Integer availableQuantity;

    private String status;

    public Long getAssetId()
    {
        return assetId;
    }

    public void setAssetId(Long assetId)
    {
        this.assetId = assetId;
    }

    public String getAssetNo()
    {
        return assetNo;
    }

    public void setAssetNo(String assetNo)
    {
        this.assetNo = assetNo;
    }

    public String getAssetName()
    {
        return assetName;
    }

    public void setAssetName(String assetName)
    {
        this.assetName = assetName;
    }

    public String getAssetType()
    {
        return assetType;
    }

    public void setAssetType(String assetType)
    {
        this.assetType = assetType;
    }

    public String getLocation()
    {
        return location;
    }

    public void setLocation(String location)
    {
        this.location = location;
    }

    public Integer getTotalQuantity()
    {
        return totalQuantity;
    }

    public void setTotalQuantity(Integer totalQuantity)
    {
        this.totalQuantity = totalQuantity;
    }

    public Integer getAvailableQuantity()
    {
        return availableQuantity;
    }

    public void setAvailableQuantity(Integer availableQuantity)
    {
        this.availableQuantity = availableQuantity;
    }

    public String getStatus()
    {
        return status;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }
}
