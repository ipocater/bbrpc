package com.ipoca.bbrpc.core.cluster;

import com.ipoca.bbrpc.core.api.Router;
import com.ipoca.bbrpc.core.meta.InstanceMeta;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * 灰度路由。
 *
 *@Author：xubang
 *@Date：2024/4/9  0:06
 */

@Slf4j
@Data
public class GrayRouter implements Router<InstanceMeta> {

    private int grayRatio;

    private final Random random = new Random();

    public GrayRouter(int grayRatio) {
        this.grayRatio = grayRatio;
    }

    @Override
    public List<InstanceMeta> route(List<InstanceMeta> providers) {

        if (providers==null||providers.size()<=1){
            return providers;
        }

        List<InstanceMeta> normalNodes = new ArrayList<>();
        List<InstanceMeta> grayNodes = new ArrayList<>();

        providers.forEach( p->{
            if ("true".equals(p.getParameter().get("gray"))){
                grayNodes.add(p);
            } else {
                normalNodes.add(p);
            }
        });

        log.debug(" grayRouter grayNodes/nornalNodes,grayRatio ===> {}/{}, {}",
                grayNodes.size(),normalNodes.size(),grayRatio);

        if (normalNodes.isEmpty() || grayNodes.isEmpty()) return providers;
        if (grayRatio <=0 ){
            return normalNodes;
        } else if (grayRatio >= 100){
            return grayNodes;
        }

        if (random.nextInt(100) < grayRatio){
            log.debug(" grayRouter grayNodes ===> {}", grayNodes);
            return grayNodes;
        } else {
            log.debug(" grayRouter normalNodes ===> {}", normalNodes);
            return normalNodes;
        }
    }
}
