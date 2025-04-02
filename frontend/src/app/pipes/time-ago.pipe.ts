import { Pipe, PipeTransform } from '@angular/core';
import { formatDistanceToNow } from 'date-fns';

@Pipe({
  name: 'timeAgo'
})
export class TimeAgoPipe implements PipeTransform {

  transform(value: Date | string | number): string  {
    if (!value) return '';
    const date = new Date(value);
    return formatDistanceToNow(date, { addSuffix: true });
  }

}
