/**
 * Mobile detection mixin.
 * Provides a reactive `isMobile` boolean that stays in sync with the Vuex device state.
 *
 * Usage:
 *   import mobileMixin from '@/mixins/mobile'
 *   export default { mixins: [mobileMixin] }
 *
 * Then use `this.isMobile` in templates / computed props / methods.
 */
import { mapState } from 'vuex'

export default {
  computed: {
    ...mapState({
      isMobile: state => state.app.device === 'mobile'
    })
  }
}
