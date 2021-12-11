#! /bin/bash
# The cwd for this script should be the server folder itself

if [ ! -d ".server" ]; then
  echo "The .server folder is missing"
  exit 1
fi

if [ -d ".server_cache" ]; then
  rm -r ./.server_cache
fi

cp -pLR ./.server ./.server_cache

if [ -d "plugins" ]; then
  cp -pLR ./plugins/* ./.server_cache/plugins
fi

cp -pLR ../build/libs/* ./.server_cache/plugins

cd ./.server_cache
./start.sh
cd ..

exit 0