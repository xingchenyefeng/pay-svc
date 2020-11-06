package com.zhch.paysvc.config;

/**
 * @author lumos
 * @date 2020/10/18 07
 */
public enum TradeStateEnum {
    //    状态码 1 创建订单 2 支付 3 退费 4 失效 5 请求支付异常 6 取消支付
    CREATED("1", "创建订单"),
    COMPLETED("2", "已支付"),
    REFUND_ALL("3", "已退费"),
    REFUND_PART("7", "有退费"),
    EXPIRE("4", "已关闭"),
    EXCEPTION("5", "异常"),
    CANCEL("6", "已取消");

    private String state;
    private String label;

    TradeStateEnum(String state, String label) {
        this.state = state;
        this.label = label;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public static String getLabelByState(String state) {
        for (TradeStateEnum stateEnum : TradeStateEnum.values()) {
            if (stateEnum.getState().equals(state)) {
                return stateEnum.getLabel();
            }
        }
        return null;
    }
}
