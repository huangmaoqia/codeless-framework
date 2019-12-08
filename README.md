1. 0行代码实现全表字段都可查询的GetApi.
- 所有记录
> 请求:http://www.hmq123.cn/user/search</br>
返回:[{"id":"1","createTime":"2019-09-02 21:40:09","updateTime":"2019-12-02 21:40:13","createrId":null,"modifierId":null,"userName":"andy"},{"id":"2","createTime":"2019-12-08 15:03:18","updateTime":"2019-12-08 15:03:21","createrId":null,"modifierId":null,"userName":"mini"},{"id":"3","createTime":"2018-02-01 15:14:45","updateTime":"2018-02-01 15:14:54","createrId":null,"modifierId":null,"userName":"cindy"}]
- 获取创建时间大于2019-8的记录
> 请求:http://www.hmq123.cn/user/search?createTime-GT=2019-8</br>
返回:[{"id":"1","createTime":"2019-09-02 21:40:09","updateTime":"2019-12-02 21:40:13","createrId":null,"modifierId":null,"userName":"andy"},{"id":"2","createTime":"2019-12-08 15:03:18","updateTime":"2019-12-08 15:03:21","createrId":null,"modifierId":null,"userName":"mini"}]
- 获取创建时间大于201908的记录且用户名像'%an%'的记录
>请求:http://www.hmq123.cn/user/search?createTime-GT=201908&userName-LIKE=an<br>
返回:[{"id":"1","createTime":"2019-09-02 21:40:09","updateTime":"2019-12-02 21:40:13","createrId":null,"modifierId":null,"userName":"andy"}]
- 分页查询,获取创建时间大于20190801的记录
>请求:http://www.hmq123.cn/user/search?createTime-GT=20190801&pageIndex=1&pageSize=2&orderBy=id&order=desc<br>
>返回:[{"id":"2","createTime":"2019-12-08 15:03:18","updateTime":"2019-12-08 15:03:21","createrId":null,"modifierId":null,"userName":"mini"},{"id":"1","createTime":"2019-09-02 21:40:09","updateTime":"2019-12-02 21:40:13","createrId":null,"modifierId":null,"userName":"andy"}]
