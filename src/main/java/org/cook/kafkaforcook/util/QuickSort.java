package org.cook.kafkaforcook.util;

import org.cook.kafkaforcook.entity.CookEntity;

import java.time.LocalTime;
import java.util.*;

public class QuickSort {

    public void quickSort(List<Integer> arrayList, int low, int high) {
        int pivot = arrayList.get((low + high) / 2);
        int leftIndex = low;
        int rightIndex = high;
        while (leftIndex <= rightIndex) {
            while (arrayList.get(leftIndex) < pivot) {
                leftIndex++;
            }
            while (arrayList.get(rightIndex) > pivot) {
                rightIndex--;
            }
            if (leftIndex <= rightIndex) {
                Collections.swap(arrayList, leftIndex, rightIndex);
                leftIndex++;
                rightIndex--;
            }
        }

        if (low < rightIndex) {
            quickSort(arrayList, low, rightIndex);
        }
        if (high > leftIndex) {
            quickSort(arrayList, leftIndex, high);
        }
    }

    public Map<CookEntity, LocalTime> quickSortAndReturnFirstFromHashMap(Map<CookEntity, LocalTime> map) {
        HashMap<CookEntity, LocalTime> returnMap = new HashMap<>();
        List<Integer> arrayList = new ArrayList<>();
        LocalTime timeToReturn;
        for (Map.Entry<CookEntity, LocalTime> entry : map.entrySet()) {
            arrayList.add(entry.getValue().toSecondOfDay());
        }
        quickSort(arrayList, 0, arrayList.size() - 1);
        timeToReturn = LocalTime.ofSecondOfDay(arrayList.get(0));
        for (Map.Entry<CookEntity, LocalTime> entry : map.entrySet()) {
            if (entry.getValue().equals(timeToReturn)) {
                returnMap.put(entry.getKey(), entry.getValue());
            }
        }
        return returnMap;
    }
}
