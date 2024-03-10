export interface Session {
  id: number,
  sessionCode: string,
  status: string,
  initiatorUserSessionId: string
  // httpSessionId: string
}
