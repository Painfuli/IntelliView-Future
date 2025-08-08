package com.example.entity;

public class RelateDTO {
    /** 用户id */
    private Integer useId;
    /** 岗位id */
    private Integer positionId;
    /** 指数 */
    private Integer index;

    public RelateDTO(Integer useId, Integer positionId, Integer index) {
        this.useId = useId;
        this.positionId = positionId;
        this.index = index;
    }

    public Integer getUseId() {
        return useId;
    }

    public void setUseId(Integer useId) {
        this.useId = useId;
    }

    public Integer getPositionId() {
        return positionId;
    }

    public void setPositionId(Integer positionId) {
        this.positionId = positionId;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }
}
