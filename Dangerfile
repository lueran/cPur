message("Hello, this worked")

changelog.check!
lint_dir = "**/reports/lint-results.xml"
Dir[lint_dir].each do |file_name|
  android_lint.skip_gradle_task = true
  android_lint.filtering = true
  android_lint.report_file = file_name
  android_lint.lint
end
apkstats.command_type=:apk_analyzer
apkstats.apk_filepath='./spec/fixture/app-base.apk'

# custom command path
# apkstats.command_path='/path/to/analysis_command'

message(apkstats.file_size)
message(apkstats.download_size)
message(apkstats.required_features)
message(apkstats.non_required_features)
message(apkstats.permissions)
message(apkstats.min_sdk)
message(apkstats.target_sdk)
message("#{apkstats.reference_count}")
message("#{apkstats.dex_count}")

apkstats.compare_with('/spec/fixture/app-other5.apk', do_report: true)