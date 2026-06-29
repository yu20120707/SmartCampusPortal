<template>
  <div class="errPage-container">
    <el-button icon="arrow-left" class="pan-back-btn" @click="back">
      返回
    </el-button>
    <el-row>
      <el-col :span="12">
        <h1 class="text-jumbo text-ginormous">
          需要登录
        </h1>
        <h2>该页面需要对应权限才能访问</h2>
        <h6>抱歉，您的当前账号没有访问该页面的权限。请尝试切换账号，或联系管理员确认权限设置。</h6>
        <ul class="list-unstyled">
          <li class="link-type">
            <router-link to="/">
              回首页
            </router-link>
          </li>
        </ul>
      </el-col>
      <el-col :span="12">
        <img :src="errGif" width="313" height="428" alt="无法访问的示意图">
      </el-col>
    </el-row>
  </div>
</template>

<script>
import errGif from '@/assets/401_images/401.gif'

export default {
  name: 'Page401',
  data() {
    return {
      errGif: errGif + '?' + +new Date()
    }
  },
  methods: {
    back() {
      if (this.$route.query.noGoBack) {
        this.$router.push({ path: '/' })
      } else {
        this.$router.go(-1)
      }
    }
  }
}
</script>

<style lang="scss" scoped>
  .errPage-container {
    width: 800px;
    max-width: 100%;
    margin: 100px auto;
    .pan-back-btn {
      background: #0D7C6B;
      color: #fff;
      border: none!important;
    }
    .pan-gif {
      margin: 0 auto;
      display: block;
    }
    .pan-img {
      display: block;
      margin: 0 auto;
      width: 100%;
    }
    .text-jumbo {
      font-size: 60px;
      font-weight: 700;
      color: #484848;
    }
    .list-unstyled {
      font-size: 14px;
      li {
        padding-bottom: 5px;
      }
      a {
        color: #0D7C6B;
        text-decoration: none;
        &:hover {
          text-decoration: underline;
        }
      }
    }
  }

  // Mobile adaptation
  @media screen and (max-width: 991px) {
    .errPage-container {
      width: 100%;
      margin: 40px auto;
      padding: 0 16px;

      .text-jumbo {
        font-size: 36px;
      }

      h2 {
        font-size: 16px;
      }

      h6 {
        font-size: 13px;
      }
    }

    .el-row {
      display: flex;
      flex-direction: column;
    }

    .el-col {
      width: 100% !important;
      max-width: 100% !important;
      flex: none !important;

      img {
        width: 100%;
        height: auto;
        margin-top: 24px;
      }
    }
  }
</style>
