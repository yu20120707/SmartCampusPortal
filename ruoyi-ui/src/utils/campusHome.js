const CAMPUS_HOME_PATH = '/campus/portal'

export function getCampusHomePath(roles = []) {
  if (!Array.isArray(roles)) {
    return CAMPUS_HOME_PATH
  }
  if (roles.some(role => ['student', 'teacher', 'leader'].includes(role))) {
    return CAMPUS_HOME_PATH
  }
  return CAMPUS_HOME_PATH
}

export function isDefaultHomePath(path) {
  return path === '/' || path === '/index'
}
