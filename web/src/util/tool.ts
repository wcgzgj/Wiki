export class Tool {
    /**
     * 空校验 null或""都返回true
     */
    public static isEmpty (obj: any) {
        if ((typeof obj === 'string')) {
            return !obj || obj.replace(/\s+/g, "") === ""
        } else {
            return (!obj || JSON.stringify(obj) === "{}" || obj.length === 0);
        }
    }

    /**
     * 非空校验
     */
    public static isNotEmpty (obj: any) {
        return !this.isEmpty(obj);
    }

    /**
     * 对象复制
     * @param obj
     */
    public static copy (obj: object) {
        if (Tool.isNotEmpty(obj)) {
            return JSON.parse(JSON.stringify(obj));
        }
    }

    /**
     * 使用递归将数组转为树形结构
     * 父ID属性为parent
     * 当初始调用的时候，因为所有的一级标签的parentID都是0，所以parentID要传入0
     */
    public static array2Tree (array: any, parentId: number) {
        // 终止条件
        if (Tool.isEmpty(array)) {
            return [];
        }

        // const result = [];
        const result: any[]=[];
        for (let i = 0; i < array.length; i++) {
            const c = array[i];
            if (Number(c.parent) === Number(parentId)) {
                // 找出一级标签节点
                result.push(c);
                // 找出所有父节点是一级标签的节点
                const children = Tool.array2Tree(array, c.id);
                // 如果获取的 children 数组非空，就将其塞给其父节点
                if (Tool.isNotEmpty(children)) {
                    // js真jb乱来，可以往对象里面随便塞其他对象
                    c.children = children;
                }
            }
        }
        return result;
    }


    /**
     * 随机生成[len]长度的[radix]进制数
     * @param len
     * @param radix 默认62
     * @returns {string}
     */
    public static uuid (len: number, radix = 62) {
        const chars = '0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz'.split('');
        const uuid = [];
        radix = radix || chars.length;

        for (let i = 0; i < len; i++) {
            // @ts-ignore
            uuid[i] = chars[0 | Math.random() * radix];
        }

        return uuid.join('');
    }
}
