package top.misec.task;

import com.google.gson.JsonObject;
import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;
import top.misec.apiquery.ApiList;
import top.misec.apiquery.oftenAPI;
import top.misec.utils.HttpUtil;

import static top.misec.task.TaskInfoHolder.STATUS_CODE_STR;
import static top.misec.task.TaskInfoHolder.userInfo;

/**
 * 银瓜子换硬币
 *
 * @author @JunzhouLiu @Kurenai
 * @since 2020-11-22 5:25
 */
@Log4j2
public class Silver2coin implements Task {

    @Override
    public void run() {

        JsonObject resultJson = HttpUtil.doGet(ApiList.silver2coin);
        int responseCode = resultJson.get(STATUS_CODE_STR).getAsInt();
        if (responseCode == 0) {
            log.info("银瓜子兑换硬币成功");
        } else {
            log.debug("银瓜子兑换硬币失败 原因是: " + resultJson.get("msg").getAsString());
        }

        JsonObject queryStatus = HttpUtil.doGet(ApiList.getSilver2coinStatus).get("data").getAsJsonObject();
        double silver2coinMoney = oftenAPI.getCoinBalance();
        log.info("当前银瓜子余额: " + queryStatus.get("silver").getAsInt());
        log.info("兑换银瓜子后硬币余额: " + silver2coinMoney);

        /*
        兑换银瓜子后，更新userInfo中的硬币值
         */
        if (userInfo != null) {
            userInfo.setMoney(silver2coinMoney);
        }
    }

    @Override
    public String getName() {
        return "银瓜子换硬币";
    }
}
