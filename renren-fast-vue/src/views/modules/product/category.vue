<template>
  <div>
    <el-switch
      v-model="draggable"
      active-text="开启拖拽"
      inactive-text="关闭拖拽">
    </el-switch>
    <el-button v-if="draggable" @click="batchSave">批量保存</el-button>
    <el-button type="danger" @click="batchDelete">批量删除</el-button>
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
      ref="menuTree"
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
      pCid: [],
      updateNodes: [],
      maxLevel: 0,
      title: '',
      dialogType: '',
      draggable: false,
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
    batchDelete () {
      let catIds = []
      let checkNodes = this.$refs.menuTree.getCheckedNodes()
      console.log('被选中的元素', checkNodes)
      for (let i = 0; i < checkNodes.length; i++) {
        catIds.push(checkNodes[i].catId)
      }
      this.$confirm(`是否批量删除【${catIds}】菜单?`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$http({
          url: this.$http.adornUrl('/product/category/delete'),
          method: 'post',
          data: this.$http.adornData(catIds, false)
        }).then(({data}) => {
          this.$message({
            message: '菜单批量删除成功',
            type: 'success'
          })
          // 刷新出新的菜单
          this.getMenus()
        })
      }).catch(() => {})
    },
    batchSave () {
      this.$http({
        url: this.$http.adornUrl('/product/category/update/sort'),
        method: 'post',
        data: this.$http.adornData(this.updateNodes, false)
      }).then(({data}) => {
        this.$message({
          message: '菜单顺序等修改成功',
          type: 'success'
        })
        // 刷新出新的菜单
        this.getMenus()
        // 设置需要默认展开的菜单
        this.expandedKey = this.pCid
        this.updateNodes = []
        this.maxLevel = 0
      })
    },
    // 拖拽成功完成时触发的事件:
    // 共四个参数，依次为：被拖拽节点对应的 Node、结束拖拽时最后进入的节点、被拖拽节点的放置位置（before、after、inner）、event
    handleDrop (draggingNode, dropNode, dropType, ev) {
      console.log('handleDrop', draggingNode, dropNode, dropType)
      // 当前节点最新父节点的id
      let pCid = 0
      let sublings = null
      if (dropType === 'before' || dropType === 'after') {
        pCid = dropNode.parent.data.catId === undefined ? 0 : dropNode.parent.data.catId
        sublings = dropNode.parent.childNodes
      } else {
        pCid = dropNode.data.catId
        sublings = dropNode.childNodes
      }
      // 当前拖拽节点的最新顺序
      for (let i = 0; i < sublings.length; i++) {
        if (sublings[i].data.catId === draggingNode.data.catId) {
          // 如果遍历的是正在拖拽的节点，还要改变父节点的id
          let catLevel = draggingNode.level
          if (sublings[i].level !== draggingNode.level) {
            // 当前节点的层级发生变化
            catLevel = sublings[i].level
            // 修改其他子节点的层级
            this.updateChildNodeLevel(sublings[i])
          }
          this.updateNodes.push({catId: sublings[i].data.catId, sort: i, parentCid: pCid, catLevel: catLevel})
        } else {
          this.updateNodes.push({catId: sublings[i].data.catId, sort: i})
        }
      }
      // 当前拖拽节点的最新层级
      console.log('updateNodes', this.updateNodes)
      this.pCid.push(pCid)
    },
    updateChildNodeLevel (node) {
      if (node.childNodes.length > 0) {
        for (let i = 0; i < node.childNodes.length; i++) {
          const cNode = node.childNodes[i].data
          this.updateNodes.push({catId: cNode.catId, catLevel: node.childNodes[i].level})
          this.updateChildNodeLevel(node.childNodes[i])
        }
      }
    },
    // 拖拽时判定目标节点能否被放置。type 参数有三种情况：'prev'、'inner' 和 'next'，分别表示放置在目标节点前、
    // 插入至目标节点和放置在目标节点后
    allowDrop (draggingNode, dropNode, type) {
      // 1.被拖拽的当前节点以及所在的父节点总层数不能大于3

      // 1).被拖住的当前节点总层数
      console.log('allowDrop:', draggingNode, dropNode, type)
      //
      this.countNodeLevel(draggingNode)
      // 当前正在拖拽的节点 + 父节点所在的深度不大于3即可
      console.log('max', this.maxLevel)
      console.log('draggingNode.level:', draggingNode.level)
      let deep = Math.abs(this.maxLevel - draggingNode.level) + 1
      console.log('深度：', deep)
      if (type === 'inner') {
        return (deep + dropNode.level) <= 3
      } else {
        return (deep + dropNode.parent.level) <= 3
      }
    },
    countNodeLevel (node) {
      // 找到所有子节点，求出最大深度
      if (node.childNodes != null && node.childNodes.length > 0) {
        for (let i = 0; i < node.childNodes.length; i++) {
          if (node.childNodes[i].level > this.maxLevel) {
            this.maxLevel = node.childNodes[i].level
          }
          this.countNodeLevel(node.childNodes[i])
        }
      }
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
      this.$confirm(`是否删除【${data.name}】菜单?`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
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
      }).catch(() => {})
    }
  },
  created () {
    this.getMenus()
  }
}
</script>

<style scoped>

</style>
