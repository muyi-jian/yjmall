<template>
  <div>
    <el-tree
      :data="menus"
      :props="defaultProps"
      :expand-on-click-node="false"
      :default-expanded-keys="expandedKey"
      show-checkbox
      :draggable="draggable"
      :allow-drop="allowDrop"
      @node-drop="handleDrop"
      node-key="catId"
    >
    <span class="custom-tree-node" slot-scope="{ node, data }">
        <span>{{ node.label }}</span>
        <span>
          <el-button
            v-if="node.level<=2"
            type="text"
            size="mini"
            @click="() => append(data)">
            Append
          </el-button>
          <el-button
            type="text"
            size="mini"
            @click="() => edit(data)">
            edit
          </el-button>
          <el-button
            v-if="node.childNodes.length===0"
            type="text"
            size="mini"
            @click="() => remove(node, data)">
            Delete
          </el-button>
        </span>
      </span>
    </el-tree>
    <el-dialog
      :title="title"
      :visible.sync="dialogVisible"
      width="30%"
      :close-on-click-modal="false"
    >
      <el-form :model="category">
        <el-form-item label="分类名称">
          <el-input v-model="category.name" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="图标">
          <el-input v-model="category.icon" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="计量单位">
          <el-input v-model="category.productUnit" autocomplete="off"></el-input>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="submitDat">确 定</el-button>
      </span>
    </el-dialog>
  </div>

</template>

<script>
export default {
  data () {
    return {
      title: '',
      dialogType: '',
      draggable: true,
      category: {
        name: '',
        parentCid: 0,
        catLevel: 0,
        showStatus: 1,
        sort: 0,
        catId: '',
        icon: '',
        productUnit: ''
      },
      menus: [],
      // dialog显示隐藏
      dialogVisible: false,
      expandedKey: [],
      defaultProps: {
        children: 'children',
        label: 'name'
      }
    }
  },
  methods: {
    getMenus () {
      this.dataListLoading = true
      this.$http({
        url: this.$http.adornUrl('/product/category/list/tree'),
        method: 'get'
      }).then(({data}) => {
        console.log('成功获取数据...', data.data)

        this.menus = data.data
      })
    },
    edit (data) {
      console.log('修改数据', data)
      this.dialogVisible = true
      this.dialogType = 'edit'
      this.title = '修改分类'
      // 查询数据，根据分类ID
      this.$http({
        url: this.$http.adornUrl(`/product/category/info/${data.catId}`),
        method: 'get'
      }).then(({data}) => {
        console.log('回显的数据', data)
        this.category.name = data.data.name
        this.category.catId = data.data.catId
        this.category.icon = data.data.icon
        this.category.productUnit = data.data.productUnit
        this.category.parentCid = data.data.parentCid
        this.category.catLevel = data.data.catLevel
        this.category.sort = data.data.sort
        this.category.showStatus = data.data.showStatus
      })
    },
    // 拖拽成功完成时触发的事件:
    // 共四个参数，依次为：被拖拽节点对应的 Node、结束拖拽时最后进入的节点、被拖拽节点的放置位置（before、after、inner）、event
    handleDrop (draggingNode, dropNode, dropType, ev) {
      console.log('handleDrop', draggingNode, dropNode, dropType)
    },
    // 拖拽时判定目标节点能否被放置。type 参数有三种情况：'prev'、'inner' 和 'next'，分别表示放置在目标节点前、
    // 插入至目标节点和放置在目标节点后
    allowDrop (draggingNode, dropNode, type) {
      console.log('allowDrop:', draggingNode, dropNode, type)
      return true
    },
    append (data) {
      this.dialogVisible = true
      this.dialogType = 'add'
      this.title = '增加分类'
      this.category.parentCid = data.catId
      // 乘以1是为了预防data.catLevel是字符串
      this.category.catLevel = data.catLevel * 1 + 1
      this.category.name = ''
      this.category.catId = null
      this.category.icon = ''
      this.category.productUnit = ''
      this.category.sort = 0
      this.category.showStatus = 1
    },
    submitDat () {
      if (this.dialogType === 'add') {
        this.addCategory()
      } else if (this.dialogType === 'edit') {
        this.editCategory()
      }
    },
    // 修改三级分类
    editCategory () {
      var {catId, name, icon, productUnit} = this.category
      var data = {catId, name, icon, productUnit}
      this.$http({
        url: this.$http.adornUrl('/product/category/update'),
        method: 'post',
        data: this.$http.adornData(data, false)
      }).then(({data}) => {
        this.$message({
          message: '菜单修改成功',
          type: 'success'
        })
        // 关闭对话框
        this.dialogVisible = false
        // 刷新出新的菜单
        this.getMenus()
        // 设置需要默认展开的菜单
        this.expandedKey = [this.category.parentCid]
      })
    },
    // 添加三级分类
    addCategory () {
      console.log('三级分类的数据', this.category)
      this.$http({
        url: this.$http.adornUrl('/product/category/save'),
        method: 'post',
        data: this.$http.adornData(this.category, false)
      }).then(({data}) => {
        this.$message({
          message: '菜单保存成功',
          type: 'success'
        })
        // 关闭对话框
        this.dialogVisible = false
        // 刷新出新的菜单
        this.getMenus()
        // 设置需要默认展开的菜单
        this.expandedKey = [this.category.parentCid]
      })
    },
    remove (node, data) {
      var ids = [data.catId]
      this.$http({
        url: this.$http.adornUrl('/product/category/delete'),
        method: 'post',
        data: this.$http.adornData(ids, false)
      }).then(({data}) => {
        this.$message({
          message: '菜单删除成功',
          type: 'success'
        })
        // 刷新菜单
        this.getMenus()
        // 设置需要默认展开的菜单
        this.expandedKey = [node.parent.data.catId]
      })
      console.log('remove', node, data)
    }
  },
  created () {
    this.getMenus()
  }
}
</script>

<style scoped>

</style>
