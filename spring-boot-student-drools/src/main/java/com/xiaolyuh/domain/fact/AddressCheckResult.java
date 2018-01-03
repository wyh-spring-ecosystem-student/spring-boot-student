package com.xiaolyuh.domain.fact;

public class AddressCheckResult {

    private boolean postCodeResult = false; // true:通过校验；false：未通过校验

    public boolean isPostCodeResult() {
        return postCodeResult;
    }

    public void setPostCodeResult(boolean postCodeResult) {
        this.postCodeResult = postCodeResult;
    }
}
