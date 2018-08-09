#include <jni.h>
#include <stdio.h>
#include <stdlib.h>

#include "lua.h"
#include "lualib.h"
#include "lauxlib.h"

#include "luasocket.h"

static int test(lua_State * L);

int test(lua_State * L) {
    luaL_requiref(L, "socket", luaopen_socket_core, 1);
    lua_pop(L, 1);  /* remove lib */
    return 0;
}