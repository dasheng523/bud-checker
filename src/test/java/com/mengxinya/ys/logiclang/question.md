# 逻辑题
## 楼层推理
小贝，小库，小赖，小米，小麦住在一个五层公寓楼的不同层
小贝不住在顶层
小库不住在底层
小赖不住在顶层也不住在底层
小米住的比小库高一层
小麦不住在小赖相邻的层
小赖不住在小库相邻的层
请问它们各住在哪层？


```javascript
var xiaobei = maybe(1, 2, 3, 4, 5);
var xiaoku = maybe(1, 2, 3, 4, 5);
var xiaolai = maybe(1, 2, 3, 4, 5);
var xiaomi = maybe(1, 2, 3, 4, 5);
var xiaomai = maybe(1, 2, 3, 4, 5);

not(xiaobei, 5);
not(xiaoku, 1);
not(xiaolai, 1);
not(xiaolai, 5);
gt(xiaomi, xiaoku);
notNear(xiaomai, xiaolai);
notNear(xiaolai, xiaoku);

print(xiaobei, xiaoku, xiaolai, xiaomi, xiaomai);
```



## 说谎者
五个女生参加一个考试，她们的家长对考试结果过分关注。为此她们约定，在给家里谈到考试时，每个女生都要说一句真话和一句假话。以下是她们说的句子：
小贝：小凯考第二，我只考了第三。
小艾：我考了第一，小丽第二。
小丽：我考第三，小艾考得最差。
小凯：我第二，小马只考了第四。
小马：我是第四，小贝成绩最高。
这五个女生实际的排名是什么？