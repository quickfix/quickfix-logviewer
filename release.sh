VERSION=$1

rm -rf release
mkdir -p release/quickfix-logviewer
rm -rf lib/*~
cp lib/* release/quickfix-logviewer
pushd release
tar czvf quickfix-logviewer-$VERSION.tar.gz quickfix-logviewer
zip -r quickfix-logviewer-$VERSION.zip quickfix-logviewer
popd