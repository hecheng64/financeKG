package com.cc.janus.enter;

import com.cc.janus.arguments.Arguments;
import com.cc.janus.dataImport.ImportEdge;
import com.cc.janus.dataImport.ImportVertex;
import com.cc.janus.schema.finalSchema;

/**
 * 统一入口
 * @author Shengjun Liu
 * @version 2018-07-26
 *
 */
public class Enter {
    public static void main(String[] args) {


        // 简单的将参数生成Map集合.

        Arguments options = new Arguments(args);
        // java.util.HashMap<String, String> options = com.xyshzh.janusgraph.utils.ArgsUtils.initOptions(args);

        if (options.isSchema()) { // Schema
            new finalSchema().execute(options.getCommonOptions().getOptionsMap());
        }
//        else if (options.isExport()) { // 导出
//            if (Boolean.valueOf(String.class.cast(options.getCommonOptions().getOptionsMap().getOrDefault("isVertex", "false")))) { // 导出点
//                new ExportVertex().execute(options.getCommonOptions().getOptionsMap());;
//            } else if (Boolean.valueOf(String.class.cast(options.getCommonOptions().getOptionsMap().getOrDefault("isEdge", "false")))) { // 导出边
//                new ExportEdge().execute(options.getCommonOptions().getOptionsMap());
//            } else {
//                System.out.println("ERROR!");
//                System.exit(1);
//            }
//        }
        else if (options.isImport()) { // 导入
            if (Boolean.valueOf(String.class.cast(options.getCommonOptions().getOptionsMap().getOrDefault("isVertex", "false")))) { // 导入点
                new ImportVertex().execute(options.getCommonOptions().getOptionsMap());
            } else if (Boolean.valueOf(String.class.cast(options.getCommonOptions().getOptionsMap().getOrDefault("isEdge", "false")))) { // 导入边
                new ImportEdge().execute(options.getCommonOptions().getOptionsMap());
            } else {
                System.out.println("ERROR!");
                System.exit(1);
            }
        } else { // 异常
            System.out.println("ERROR!");
            System.exit(1);
        }

    }
}
