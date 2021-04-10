package top.faroz.job;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import top.faroz.service.DocService;
import top.faroz.util.SnowFlake;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @ClassName TestJob
 * @Description TODO
 * @Author FARO_Z
 * @Date 2021/4/10 下午2:18
 * @Version 1.0
 **/
@Component
public class DocJob {
    private static final Logger LOG = LoggerFactory.getLogger(DocJob.class);

    @Resource
    private DocService docService;

    @Resource
    private SnowFlake snowFlake;


    /**
     * 自定义cron表达式跑批
     * 只有等上一次执行完成，下一次才会在下一个时间点执行，错过就错过
     * cron 表达式不用记，可以在这个网址生成：https://cron.qqe2.com/
     * <p>
     * 下面这段表示：每隔 30 秒，更新一次电子书信息
     */
    @Scheduled(cron = "1/30 * * * * ? ")
    public void cron() throws InterruptedException {

        //增加日志流水号
        MDC.put("LOG_ID", String.valueOf(snowFlake.nextId()));

        LOG.info("更新电子书下的文档数据开始");
        long start = System.currentTimeMillis();
        docService.updateDocInfo();
        LOG.info("更新电子书下的文档数据结束，耗时：{}毫秒", System.currentTimeMillis() - start);
    }
}
