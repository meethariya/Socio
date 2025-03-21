import { task, src, dest } from 'gulp';
import gzip from 'gulp-gzip';

task('compress', function () {
  return src(['./dist/**/*.*'])
    .pipe(gzip())
    .pipe(dest('./dist'));
});
