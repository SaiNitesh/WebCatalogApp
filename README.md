# WebCatalogApp
Android app  - Web Catalog
Video: https://www.youtube.com/watch?v=7wclCdJ4Q3g

This app is used to connect and download json data from rest service. When data is downloaded it is stored on Ram chache,
if Ram chache is full the data is stored temporary on Disk using DiskLruCache library. Users can interact with GUI selecting categories and viewing  items. Pictures of items can be zoomed in and zoomed out. ViewPager starts tasks in backgrounds that animates swiping images on display.

List of some used libraries:
  - LruCache
  - DiskLruCache
  - RecycleList
  - CardView
  - Volley
  - ViewPager
  - SyncTask
  - ChrisBanes.photoview
  - ToolBar
