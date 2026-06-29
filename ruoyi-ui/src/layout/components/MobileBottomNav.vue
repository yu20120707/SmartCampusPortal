<template>
  <div v-if="isMobile && tabs.length > 0" class="mobile-bottom-nav">
    <div
      v-for="tab in tabs"
      :key="tab.path"
      class="nav-tab"
      :class="{ active: isActive(tab) }"
      @click="go(tab)"
    >
      <svg-icon :icon-class="tab.icon" class="nav-icon" />
      <span class="nav-label">{{ tab.title }}</span>
    </div>
  </div>
</template>

<script>
import mobileMixin from '@/mixins/mobile'

export default {
  name: 'MobileBottomNav',
  mixins: [mobileMixin],
  data() {
    return {
      tabs: []
    }
  },
  computed: {
    sidebarRouters() {
      return this.$store.getters.sidebarRouters
    }
  },
  watch: {
    sidebarRouters: {
      immediate: true,
      handler(routes) {
        // Extract top-level visible routes with icons for bottom nav (max 5)
        this.tabs = (routes || [])
          .filter(r => !r.hidden && r.meta && r.meta.icon)
          .slice(0, 5)
          .map(r => ({
            path: this.resolvePath(r),
            title: r.meta.title,
            icon: r.meta.icon
          }))
      }
    }
  },
  methods: {
    resolvePath(route) {
      if (route.path.startsWith('/')) return route.path
      return '/' + route.path
    },
    isActive(tab) {
      return this.$route.path.startsWith(tab.path)
    },
    go(tab) {
      if (!this.isActive(tab)) {
        this.$router.push(tab.path)
      }
    }
  }
}
</script>

<style lang="scss" scoped>
.mobile-bottom-nav {
  position: fixed;
  bottom: 0;
  left: 0;
  right: 0;
  height: 56px;
  background: #fff;
  display: flex;
  justify-content: space-around;
  align-items: center;
  border-top: 1px solid #e4e7ed;
  z-index: 1000;
  box-shadow: 0 -1px 4px rgba(0, 0, 0, 0.06);
  padding-bottom: env(safe-area-inset-bottom);

  .nav-tab {
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    padding: 4px 6px;
    min-width: 48px;
    min-height: 48px;
    cursor: pointer;
    transition: color 0.2s;
    color: #909399;
    -webkit-tap-highlight-color: rgba(13, 124, 107, 0.15);
    user-select: none;

    &.active {
      color: #0D7C6B;
    }

    .nav-icon {
      font-size: 20px;
      width: 20px;
      height: 20px;
      margin-bottom: 1px;
    }

    .nav-label {
      font-size: 10px;
      line-height: 1.2;
    }
  }
}
</style>
